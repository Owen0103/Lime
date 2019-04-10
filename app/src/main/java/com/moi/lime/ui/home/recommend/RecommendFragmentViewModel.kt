package com.moi.lime.ui.home.recommend

import androidx.lifecycle.ViewModel
import com.lime.testing.OpenForTesting
import com.moi.lime.core.livedata.SingleLiveEvent
import com.moi.lime.repository.LimeRepository
import com.moi.lime.util.switchMap
import javax.inject.Inject

@OpenForTesting
class RecommendFragmentViewModel @Inject constructor(private val limeRepository: LimeRepository) : ViewModel() {
    val fetchRecommendTrigger = SingleLiveEvent<Boolean>()
    val recommendResource = fetchRecommendTrigger.switchMap {
        limeRepository.fetchRecommendMusics(it)
    }

}