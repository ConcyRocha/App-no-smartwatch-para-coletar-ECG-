package br.concy.demo.view

sealed class HomeUIState {
    data object Default: HomeUIState()
    data object Collecting: HomeUIState()
}
