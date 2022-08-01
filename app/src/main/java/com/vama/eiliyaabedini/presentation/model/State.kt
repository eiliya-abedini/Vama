package com.vama.eiliyaabedini.presentation.model

sealed class State<DATA_MODEL, ACTION> {

    class LoadingState<DATA_MODEL, ACTION> : State<DATA_MODEL, ACTION>()

    data class DataState<DATA, ACTION>(val data: DATA) : State<DATA, ACTION>()

    data class ErrorState<DATA_MODEL, ACTION>(val error: String) : State<DATA_MODEL, ACTION>()

    data class Events<DATA_MODEL, ACTION>(val action: ACTION) : State<DATA_MODEL, ACTION>()
}
