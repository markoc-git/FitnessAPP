package com.example.fitnessapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder as Builder

class MainActivity : Activity() {

    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure sign-in to request the user's ID, email, and basic profile.
        val gso: GoogleSignInOptions = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set the sign-in button to trigger the sign-in flow.
        val signInButton: SignInButton = findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener(View.OnClickListener {
            // Check if the user is already signed in
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                // User is already signed in
                val id: String? = account.id
                val email: String? = account.email
                val name: String? = account.displayName

                // Display the user's information on the screen.
                Toast.makeText(this, "Already signed in!\nID: $name\nEmail: $email\nName: $name", Toast.LENGTH_LONG)
                    .show()
            } else {
                // User is not signed in, initiate sign-in process
                signIn()
            }
        })
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from the sign-in activity is available.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            // Get the user's ID, email, and basic profile information.
            val id: String? = account.getId()
            val email: String? = account.getEmail()
            val name: String? = account.getDisplayName()

            // Display the user's information on the screen.
            Toast.makeText(this, "ID: $id\nEmail: $email\nName: $name", Toast.LENGTH_LONG)
                .show()
        } catch (e: ApiException) {
            Toast.makeText(this, "Sign-in failed: " + e.message, Toast.LENGTH_LONG).show()
        }
    }
   
    companion object {
        private const val RC_SIGN_IN = 9001
    }
}