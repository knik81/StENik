package best.mobile.entities

sealed class ResultStENik<out T> {
    data class Success<out T>(val data: T) : ResultStENik<T>()
    data class Error(val message: String) : ResultStENik<Nothing>()
}