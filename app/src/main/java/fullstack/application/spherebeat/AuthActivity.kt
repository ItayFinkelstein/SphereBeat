package fullstack.application.spherebeat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.progressindicator.CircularProgressIndicator

class AuthActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var progressBar: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_auth)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController?.let {
            NavigationUI.setupActionBarWithNavController(
                activity = this,
                navController = it
            )
        }
        // Initialize the ProgressBar
        progressBar = findViewById(R.id.progressBar)
    }

    fun showProgressBar() {
        progressBar.visibility = CircularProgressIndicator.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = CircularProgressIndicator.INVISIBLE
    }
}