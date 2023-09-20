package com.dhandev.eepa.ui.news

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dhandev.eepa.databinding.FragmentNewsBinding
import com.dhandev.eepa.divkit.GlideDivImageLoader
import com.dhandev.eepa.helper.customTab
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.DivViewFacade
import com.yandex.div.core.utils.IOUtils
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div2.DivAction
import com.yandex.div2.DivData
import org.json.JSONObject

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var dataUrl: String = ""

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var preferences: SharedPreferences
    private lateinit var div2View: Div2View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.symmetry.setOnClickListener{
            dataUrl = "https://www-symmetrymagazine-org.translate.goog/?_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=wapp"
            customTab.open(requireContext(), dataUrl)
        }

        binding.nature.setOnClickListener{
            dataUrl = "https://www-nature-com.translate.goog/subjects/particle-physics?error=cookies_not_supported&code=fd9ff9c4-4d59-4984-9fdf-4255a2e89596&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=wapp"
            customTab.open(requireContext(), dataUrl)
        }

        binding.scinews.setOnClickListener{
            dataUrl = "https://www-sciencenews-org.translate.goog/topic/particle-physics?_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=wapp"
            customTab.open(requireContext(), dataUrl)
        }

        binding.newsci.setOnClickListener {
            dataUrl = "https://www-newscientist-com.translate.goog/article-topic/particle-physics/?_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=wapp"
            customTab.open(requireContext(), dataUrl)
        }

        binding.scitech.setOnClickListener {
            dataUrl = "https://scitechdaily-com.translate.goog/tag/particle-physics/?_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=wapp"
            customTab.open(requireContext(), dataUrl)
        }

        binding.phyorg.setOnClickListener {
            dataUrl = "https://phys-org.translate.goog/tags/particle+physics/?_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=wapp"
            customTab.open(requireContext(), dataUrl)
        }

        preferences = requireActivity().getSharedPreferences(
            "news_ui_prefs",
            AppCompatActivity.MODE_PRIVATE
        )

        val div2Context = Div2Context(
            baseContext = requireActivity(),
            configuration = DivConfiguration.Builder(GlideDivImageLoader(requireActivity()))
                .actionHandler(object : DivActionHandler() {
                    override fun handleAction(action: DivAction, view: DivViewFacade): Boolean {
                        Log.d("DivKit", "handleAction: $action")
                        return super.handleAction(action, view)
                    }
                })
                .supportHyphenation(true)
                .visualErrorsEnabled(true)
                .build()
        )
        div2View = Div2View(div2Context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        binding.linearLayout.addView(div2View)
        setDivData()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.text.observe(viewLifecycleOwner) {
            setDivData(it)
            preferences.edit {
                putString("news_ui", it)
            }
        }
    }

    private fun setDivData(data: String? = null) {
        val json = data
            ?: preferences.getString("news_ui", null)
            ?: IOUtils.toString(requireContext().assets.open("news_ui.json"))

        val divJson = JSONObject(json)
        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val parsingEnvironment = DivParsingEnvironment(ParsingErrorLogger.ASSERT)
            .apply {
                if (templatesJson != null) parseTemplates(templatesJson)
            }

        val divData = DivData(parsingEnvironment, cardJson)
        div2View.setData(divData, DivDataTag(divData.logId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
