package com.apimorlabs.reluct.compose.ui.components.bottomSheet.addEditTask

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.common.models.util.time.TimeUtils
import com.apimorlabs.reluct.compose.ui.Res
import com.apimorlabs.reluct.compose.ui.components.buttons.ReluctButton
import com.apimorlabs.reluct.compose.ui.negative_dialog_button
import com.apimorlabs.reluct.compose.ui.positive_dialog_button
import com.apimorlabs.reluct.compose.ui.theme.Dimens
import com.apimorlabs.reluct.compose.ui.theme.Shapes
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import work.racka.reluct.compose.common.date.time.picker.core.DateTimeDialogButtonText
import work.racka.reluct.compose.common.date.time.picker.core.DateTimeDialogProperties
import work.racka.reluct.compose.common.date.time.picker.core.DateTimeDialogState
import work.racka.reluct.compose.common.date.time.picker.core.rememberDateTimeDialogState
import work.racka.reluct.compose.common.date.time.picker.date.DatePicker
import work.racka.reluct.compose.common.date.time.picker.time.Timepicker

@Composable
internal fun DateTimePills(
    currentLocalDateTime: LocalDateTime,
    onLocalDateTimeChange: (dateTime: LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    dialogShape: Shape = Shapes.large,
    hasError: Boolean = false,
    errorText: String = "",
) {
    val dateDialogState = rememberDateTimeDialogState(
        dialogProperties = DateTimeDialogProperties(shape = dialogShape),
        buttonText = DateTimeDialogButtonText(
            positiveText = stringResource(Res.string.positive_dialog_button),
            negativeText = stringResource(Res.string.negative_dialog_button)
        )
    )
    val timeDialogState = rememberDateTimeDialogState(
        dialogProperties = DateTimeDialogProperties(
            desktopWindowSize = DpSize(600.dp, 400.dp),
            shape = dialogShape
        ),
        buttonText = DateTimeDialogButtonText(
            positiveText = stringResource(Res.string.positive_dialog_button),
            negativeText = stringResource(Res.string.negative_dialog_button)
        )
    )

    val pillContainerColor by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )
    val pillContentColor by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.onError
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )

    val dateString = rememberSaveable(currentLocalDateTime) {
        mutableStateOf(
            TimeUtils.getFormattedDateString(
                dateTime = currentLocalDateTime.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    val timeString = rememberSaveable(currentLocalDateTime) {
        mutableStateOf(
            TimeUtils.getTimeFromLocalDateTime(
                dateTime = currentLocalDateTime.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    DateAndTimeMaterialDialogs(
        initialLocalDateTime = currentLocalDateTime,
        dateDialogState = dateDialogState,
        timeDialogState = timeDialogState,
        onLocalDateTimeChange = { dateChange, timeChange ->
            var dateTime = currentLocalDateTime
            dateChange?.let { dateTime = it }
            timeChange?.let { dateTime = it }
            onLocalDateTimeChange(dateTime)
        },
    )

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            modifier = modifier
                .fillMaxWidth()
        ) {
            // Date
            ReluctButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = dateString.value,
                icon = Icons.Rounded.DateRange,
                onButtonClicked = { dateDialogState.show() },
                shape = Shapes.large,
                contentColor = pillContentColor,
                buttonColor = pillContainerColor
            )

            // Time
            ReluctButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = timeString.value,
                icon = Icons.Rounded.Schedule,
                onButtonClicked = { timeDialogState.show() },
                shape = Shapes.large,
                contentColor = pillContentColor,
                buttonColor = pillContainerColor
            )
        }

        if (hasError && errorText.isNotBlank()) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun DateAndTimeMaterialDialogs(
    initialLocalDateTime: LocalDateTime,
    dateDialogState: DateTimeDialogState,
    timeDialogState: DateTimeDialogState,
    onLocalDateTimeChange: (dateChange: LocalDateTime?, timeChange: LocalDateTime?) -> Unit,
) {
    // Date
    // TODO: Replace with material3
    DatePicker(
        initialDate = LocalDate(
            initialLocalDateTime.year,
            initialLocalDateTime.monthNumber,
            initialLocalDateTime.dayOfMonth
        ),
        dialogState = dateDialogState
    ) { date ->
        val dateTime = LocalDateTime(
            date.year,
            date.monthNumber,
            date.dayOfMonth,
            initialLocalDateTime.hour,
            initialLocalDateTime.minute,
            initialLocalDateTime.second,
            initialLocalDateTime.nanosecond
        )
        onLocalDateTimeChange(dateTime, null)
    }

    // Time
    // TODO: Replace with material3
    Timepicker(
        dialogState = timeDialogState,
        initialTime = initialLocalDateTime
    ) { dateTime ->
        val newDateTime = LocalDateTime(
            initialLocalDateTime.year,
            initialLocalDateTime.monthNumber,
            initialLocalDateTime.dayOfMonth,
            dateTime.hour,
            dateTime.minute,
            dateTime.second,
            dateTime.nanosecond
        )
        onLocalDateTimeChange(null, newDateTime)
    }
}