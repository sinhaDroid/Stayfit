package com.stayfit.app

class Calories {
    var totalcalories: Float = 0.toFloat()
    var totalfat: Float = 0.toFloat()
    var totalcarbs: Float = 0.toFloat()
    var totalprotein: Float = 0.toFloat()

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Calories.class)
    }

    constructor(totalcalories: Float, totalfat: Float, totalcarbs: Float, totalprotein: Float) {
        this.totalcalories = totalcalories
        this.totalfat = totalfat
        this.totalcarbs = totalcarbs
        this.totalprotein = totalprotein
    }
}