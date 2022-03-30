package com.nyan.data.util

interface Screen<State, Event> {
    fun handleState(state: State)
    fun handleEvent(event: Event)
}