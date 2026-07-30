package top.niunaijun.blackboxa.view.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import top.niunaijun.blackboxa.R
import top.niunaijun.blackboxa.databinding.ActivityCameraSettingsBinding
import top.niunaijun.blackboxa.util.inflate
import top.niunaijun.blackboxa.util.toast

class CameraSettingActivity : AppCompatActivity() {

    private val viewBinding: ActivityCameraSettingsBinding by inflate()
    private var mMethodType = 0

    private val openDocumentedResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewBinding.protectPath.setText(uri.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        title = getString(R.string.camera_setting)

        initViews()
        loadCurrentSettings()
    }

    private fun initViews() {
        viewBinding.protectMethodBtn.setOnClickListener { v ->
            val popup = PopupMenu(this, v)
            popup.menuInflater.inflate(R.menu.camera_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.protect_method_disable_camera -> onMethodTypeClick(1)
                    R.id.protect_method_local -> onMethodTypeClick(2)
                    R.id.protect_method_network -> onMethodTypeClick(3)
                }
                true
            }
            popup.show()
        }

        viewBinding.protectVideoSelect.setOnClickListener {
            openDocumentedResult.launch("video/*")
        }

        viewBinding.protectSave.setOnClickListener {
            saveSettings()
        }
    }

    private fun loadCurrentSettings() {
        val prefs = getSharedPreferences("camera_protection", Context.MODE_PRIVATE)
        val methodType = prefs.getInt("method_type", 1)
        onMethodTypeClick(methodType)
    }

    private fun onMethodTypeClick(type: Int) {
        mMethodType = type
        when (type) {
            1 -> {
                viewBinding.protectMethodText.text = getString(R.string.protect_method_disable_camera)
                viewBinding.protectTip.text = getString(R.string.protect_tip_disable)
                viewBinding.protectPath.visibility = android.view.View.GONE
                viewBinding.protectVideoSelect.visibility = android.view.View.GONE
                viewBinding.protectAudio.visibility = android.view.View.GONE
                viewBinding.protectAudioSwitch.visibility = android.view.View.GONE
            }
            2 -> {
                viewBinding.protectMethodText.text = getString(R.string.protect_method_local)
                viewBinding.protectTip.text = getString(R.string.protect_tip_local)
                viewBinding.protectPath.visibility = android.view.View.VISIBLE
                viewBinding.protectVideoSelect.visibility = android.view.View.VISIBLE
                viewBinding.protectVideoSelect.isEnabled = true
                viewBinding.protectVideoSelect.text = getString(R.string.choise_video)
                viewBinding.protectAudio.visibility = android.view.View.VISIBLE
                viewBinding.protectAudioSwitch.visibility = android.view.View.VISIBLE
                viewBinding.protectPath.isEnabled = false
                val prefs = getSharedPreferences("camera_protection", Context.MODE_PRIVATE)
                viewBinding.protectPath.setText(prefs.getString("video_path_local", ""))
                viewBinding.protectAudioSwitch.isChecked = prefs.getBoolean("video_path_local_audio_enable", true)
            }
            3 -> {
                viewBinding.protectMethodText.text = getString(R.string.protect_method_network)
                viewBinding.protectTip.text = getString(R.string.protect_tip_network)
                viewBinding.protectPath.visibility = android.view.View.VISIBLE
                viewBinding.protectVideoSelect.visibility = android.view.View.GONE
                viewBinding.protectAudio.visibility = android.view.View.VISIBLE
                viewBinding.protectAudioSwitch.visibility = android.view.View.VISIBLE
                viewBinding.protectPath.hint = getString(R.string.protect_path_hint)
                viewBinding.protectPath.isEnabled = true
                val prefs = getSharedPreferences("camera_protection", Context.MODE_PRIVATE)
                viewBinding.protectPath.setText(prefs.getString("video_path_network", ""))
                viewBinding.protectAudioSwitch.isChecked = prefs.getBoolean("video_path_network_audio_enable", true)
            }
        }
    }

    private fun saveSettings() {
        val prefs = getSharedPreferences("camera_protection", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        when (mMethodType) {
            1 -> {
                editor.putInt("method_type", 1)
                editor.putBoolean("enabled", true)
                toast(getString(R.string.save_success))
            }
            2 -> {
                val path = viewBinding.protectPath.text.toString()
                if (TextUtils.isEmpty(path)) {
                    toast(getString(R.string.video_not_set))
                    return
                }
                editor.putInt("method_type", 2)
                editor.putBoolean("enabled", true)
                editor.putString("video_path_local", path)
                editor.putBoolean("video_path_local_audio_enable", viewBinding.protectAudioSwitch.isChecked)
                toast(getString(R.string.save_success))
            }
            3 -> {
                val url = viewBinding.protectPath.text.toString()
                if (TextUtils.isEmpty(url)) {
                    toast(getString(R.string.video_not_set))
                    return
                }
                if (!url.lowercase().startsWith("http")) {
                    toast(getString(R.string.url_should_start_http))
                    return
                }
                editor.putInt("method_type", 3)
                editor.putBoolean("enabled", true)
                editor.putString("video_path_network", url)
                editor.putBoolean("video_path_network_audio_enable", viewBinding.protectAudioSwitch.isChecked)
                toast(getString(R.string.save_success))
            }
        }
        editor.apply()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CameraSettingActivity::class.java))
        }
    }
}
