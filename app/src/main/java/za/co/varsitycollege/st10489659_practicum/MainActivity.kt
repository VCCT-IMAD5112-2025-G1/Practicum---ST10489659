package za.co.varsitycollege.st10489659_practicum

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@SuppressLint("ParcelCreator")
data class PlaylistItem(
    val song: String,
    val artist: String,
    val rate: String,
    val comment: String
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }
}

class MainActivity : AppCompatActivity() {

    private val playlistItems = mutableListOf<PlaylistItem>() // Use the data class

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val addButton = findViewById<Button>(R.id.addButton)
        val viewPlaylistButton =
            findViewById<Button>(R.id.viewPlaylistButton) // Renamed for clarity
        val exitButton = findViewById<Button>(R.id.exitButton)

        addButton.setOnClickListener {
            showAddItemDialog()
        }

        viewPlaylistButton.setOnClickListener {
            if (playlistItems.isEmpty()) {
                Toast.makeText(this, "Playlist is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, DetailedViewActivity::class.java)
            // Pass the whole list of PlaylistItem objects
            intent.putParcelableArrayListExtra("PLAYLIST_ITEMS_EXTRA", ArrayList(playlistItems))
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            finishAffinity() // Closes all activities of this app
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showAddItemDialog() {
        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_playlist, null)
        val songEditText = dialogView.findViewById<EditText>(R.id.editDialogSong)
        val artistEditText = dialogView.findViewById<EditText>(R.id.editDialogArtist)
        val rateEditText = dialogView.findViewById<EditText>(R.id.editDialogRate)
        val commentEditText = dialogView.findViewById<EditText>(R.id.editDialogComment)

        AlertDialog.Builder(this)
            .setTitle("Add to Playlist")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val song = songEditText.text.toString().trim()
                val artist = artistEditText.text.toString().trim()
                val rate = rateEditText.text.toString().trim()
                val comment = commentEditText.text.toString().trim()

                if (song.isNotEmpty() && artist.isNotEmpty()) {
                    val newItem = PlaylistItem(song, artist, rate, comment)
                    playlistItems.add(newItem)
                    Toast.makeText(this, "'$song' added to playlist", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Song Title and Artist Name cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                dialog.dismiss() // Dismiss the dialog
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
            }
            .show()
    }
}
