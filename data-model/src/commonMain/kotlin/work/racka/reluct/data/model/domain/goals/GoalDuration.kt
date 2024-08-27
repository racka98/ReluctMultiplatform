package work.racka.reluct.data.model.domain.goals

import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.data.model.util.time.Week

data class GoalDuration(
    val goalInterval: GoalInterval,
    val timeRangeInMillis: LongRange?,
    val formattedTimeRange: ClosedRange<String>?,
    val selectedDaysOfWeek: ImmutableList<Week>
)