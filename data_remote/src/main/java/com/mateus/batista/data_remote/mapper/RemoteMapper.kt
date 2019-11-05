package com.mateus.batista.data_remote.mapper

interface RemoteMapper<in A, out M> {
    fun toData(model: A): M
}