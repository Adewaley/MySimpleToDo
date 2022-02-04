package com.example.mysimpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.ButtonBarLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // 1. remove the item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        // Lets detect when the user presses the add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            // code in here is going to be executed when the user clicks on a button
//            Log.i("Wale", "User clicked on button")
//        }

//        listOfTasks.add("Do laundry")
//        listOfTasks.add("Go for a walk")

        // instead of hard-coding, we load the list of tasks
        loadItems()

        // Lookup the recycler view in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // set up the button and input field,
        // so that the user can enter a task

        // get a reference to the button
        // and then set onClickListener
        findViewById<Button>(R.id.button).setOnClickListener{
            // 1. grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2. Then add the string to the list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // Notify the data adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size -1)

            // 3. Reset the text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // save the data that the user has inputted
    //save data by reading/writing from a file

    // get the file we need
    fun getDataFile(): File{

        // every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every lien in the data file
    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    // save items by writing them into our data
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

}