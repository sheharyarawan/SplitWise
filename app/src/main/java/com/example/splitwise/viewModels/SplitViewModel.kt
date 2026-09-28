package com.example.splitwise.viewModels

import androidx.lifecycle.ViewModel
import com.example.splitwise.model.User

class SplitViewModel : ViewModel() {

    var equallyUsers: List<User> = emptyList()

    var unequallyAmounts: Map<String, Double> = emptyMap()

    var percentageValues: Map<String, Double> = emptyMap()
}