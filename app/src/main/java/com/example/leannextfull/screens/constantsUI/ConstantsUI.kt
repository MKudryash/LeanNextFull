package com.example.leannextfull.screens.constantsUI

import com.example.leannextfull.R
import com.example.leannextfull.screens.modelsUI.ConnectionWithUs

/**Константы для обратной связи*/
object ConstantsUI {
    var  connectionWithUs = listOf<ConnectionWithUs>(
        ConnectionWithUs(R.drawable.telegram,"Telegram","https://t.me/SergeiKorchagin"),
        ConnectionWithUs(R.drawable.phone,"Позвонить (Иван)","tel:+79307014004"),
        ConnectionWithUs(R.drawable.book,"Что почитать","https://ridero.ru/books/povyshenie_effektivnosti_proizvodstvennoi_sistemy_kompanii/"),
        )
}