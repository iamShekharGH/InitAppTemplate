package com.shekhargh.reminderApp.domain

interface UseCase<in I, out O> {
    suspend operator fun invoke(input: I): O
}

interface NoOutputUseCase<in I> {
    suspend operator fun invoke(input: I)
}

interface NoParameterUseCase<out O> {
    suspend operator fun invoke(): O
}
/*
interface NoParameterSuspendUseCase<out O> {
    suspend operator fun invoke(): O
}*/

