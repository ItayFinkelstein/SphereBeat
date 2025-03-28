package fullstack.application.spherebeat.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import fullstack.application.spherebeat.MainActivity
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.repository.UserRepository

class AuthActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var progressBar: CircularProgressIndicator
    private val userRepository: UserRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_auth)

        if (userRepository.getLoggedUser() != null) {
            // User is not signed in, navigate to the login screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.navigate(R.id.welcomeFragment)

    }
}