package com.example.exchangeapp

import android.os.Parcel
import android.os.Parcelable
import java.util.Currency

data class Request(
    val table: String,
    val no: String,
    val effectiveDate: String,
    val rates: List<ExchangeRate>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("rates")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(table)
        parcel.writeString(no)
        parcel.writeString(effectiveDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Request> {
        override fun createFromParcel(parcel: Parcel): Request {
            return Request(parcel)
        }

        override fun newArray(size: Int): Array<Request?> {
            return arrayOfNulls(size)
        }
    }
}

data class ExchangeRate(
    val currency: String,
    val code: String,
    val mid: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currency)
        parcel.writeString(code)
        parcel.writeDouble(mid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExchangeRate> {
        override fun createFromParcel(parcel: Parcel): ExchangeRate {
            return ExchangeRate(parcel)
        }

        override fun newArray(size: Int): Array<ExchangeRate?> {
            return arrayOfNulls(size)
        }
    }
}