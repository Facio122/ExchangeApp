package com.example.exchangeapp

import android.os.Parcel
import android.os.Parcelable

class Field() :Parcelable {

    var currency = "default"
    var code = "default"
    var rate = 0.0

    constructor(parcel: Parcel) : this() {
        currency = parcel.readString().toString()
        code = parcel.readString().toString()
        rate = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currency)
        parcel.writeString(code)
        parcel.writeDouble(rate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Field> {
        override fun createFromParcel(parcel: Parcel): Field {
            return Field(parcel)
        }

        override fun newArray(size: Int): Array<Field?> {
            return arrayOfNulls(size)
        }
    }


}