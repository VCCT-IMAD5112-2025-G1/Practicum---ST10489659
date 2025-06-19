package za.co.varsitycollege.st10489659_practicum

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Collections
import kotlin.collections.isNotEmpty
import kotlin.collections.shuffle

class DetailedViewActivity : AppCompatActivity() {

    private lateinit var listViewPlaylist: ListView
    private var currentPlaylistItems: ArrayList<PlaylistItem>? = null // Keep a reference to the mutable list if needed for actions
    private lateinit var displayAdapter: ArrayAdapter<String> // Keep adapter reference to modify it
    private val displayList = mutableListOf<String>() // Keep the list of strings for display

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detailed_view)
        listViewPlaylist = findViewById(R.id.listViewPlaylist)
        val buttonShuffle = findViewById<Button>(R.id.buttonShuffle)
        val buttonClearAll = findViewById<Button>(R.id.buttonClearAll)
        val buttonBackToMain = findViewById<Button>(R.id.buttonBackToMain)

        // Initialize adapter first
        displayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        listViewPlaylist.adapter = displayAdapter

        // Retrieve the list of PlaylistItem objects
        val receivedItems = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("PLAYLIST_ITEMS_EXTRA", PlaylistItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra<PlaylistItem>("PLAYLIST_ITEMS_EXTRA")
        }

        if (receivedItems != null && receivedItems.isNotEmpty()) {
            currentPlaylistItems = ArrayList(receivedItems) // Make a mutable copy
            updateDisplayList(currentPlaylistItems!!) // Now update the display list
        } else {
            Toast.makeText(this, "No items in playlist to display.", Toast.LENGTH_LONG).show()
            currentPlaylistItems = ArrayList() // Initialize to avoid null issues
            updateDisplayList(currentPlaylistItems!!) // Show empty state, adapter will be notified
        }
        // No need to call displayAdapter.notifyDataSetChanged() here separately
        // if updateDisplayList handles it or if the adapter is populated correctly initially.

        supportActionBar?.title = "Playlist Details"

        buttonShuffle.setOnClickListener {
            shufflePlaylist()
        }
        buttonClearAll.setOnClickListener {
            confirmClearAll()
        }
        buttonBackToMain.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateDisplayList(items: List<PlaylistItem>) {
        displayList.clear()
        if (items.isNotEmpty()) {
            displayList.addAll(items.map { item ->
                "Song: ${item.song}\nArtist: ${item.artist}\nRating: ${item.rate}\nComment: ${item.comment}"
            })
        } else {
            displayList.add("Playlist is empty.") // Show message in ListView if empty
        }
        // Notify the adapter that the underlying data has changed
        if (::displayAdapter.isInitialized) { // Check if adapter is initialized
            displayAdapter.notifyDataSetChanged()
        }
    }


    private fun shufflePlaylist() {
        if (currentPlaylistItems != null && currentPlaylistItems!!.size > 1) {
            Collections.shuffle(currentPlaylistItems!!) // Shuffles the underlying data list
            updateDisplayList(currentPlaylistItems!!) // Update the strings for display
            displayAdapter.notifyDataSetChanged() // Notify the adapter
            Toast.makeText(this, "Playlist shuffled!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Not enough items to shuffle.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun clearDisplayedPlaylist() {
        currentPlaylistItems?.clear() // Clear the data
        updateDisplayList(currentPlaylistItems ?: listOf()) // Update display strings
        displayAdapter.notifyDataSetChanged() // Notify adapter
        Toast.makeText(this, "Displayed playlist cleared.", Toast.LENGTH_SHORT).show()
        // Note: This only clears the list in this activity's memory.
        // The original list in MainActivity is not affected unless you specifically
        // pass data back and handle it there.
    }
                    private fun confirmClearAll() {
                if (currentPlaylistItems.isNullOrEmpty()){
                    Toast.makeText(this, "Playlist is already empty.", Toast.LENGTH_SHORT).show()
                    return
                }

                AlertDialog.Builder(this)
                    .setTitle("Clear Playlist")
                    .setMessage("Are you sure you want to clear all items from this display? This won't affect the main list.")
                    .setPositiveButton("Clear") { _, _ ->
                        clearDisplayedPlaylist()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }


    // Since we have an explicit "Back to Main" button, the Up button functionality
    // via onSupportNavigateUp() might be redundant or you might want it to behave the same.
    // If you remove setDisplayHomeAsUpEnabled(true), this won't be called by an Up arrow.
    // override fun onSupportNavigateUp(): Boolean {
    //     finish()
    //     return true
    }
