package com.main.data

data class CountFacet(
    val addFacet: List<Count>,
    val changeFacet: List<Count>,
    val nochangeFacet: List<Count>,
    val cancelFacet: List<Count>,
    val errorFacet: List<Count>,
    val geoFacet:List<Count>,
    val addressFacet:List<Count>
)

data class Errors(val error: String)
