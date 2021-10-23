package com.example.flickerphotos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_ph.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var Rc: RecyclerView
    private lateinit var Fullimage:ImageView
    private  lateinit var ET:EditText
    private lateinit var b:Button

    private lateinit var photos: ArrayList<photoClass>
    var data:String=""
    var tag="cat"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        photos =ArrayList()
        Rc=findViewById(R.id.rv)
        Fullimage=findViewById(R.id.fullImage)
        ET=findViewById(R.id.et)
        b=findViewById(R.id.b)
        Log.d("create","create")
        Rc.adapter = adapterPh(this, photos)
        Rc.layoutManager = LinearLayoutManager(this)
        b.setOnClickListener {
            tag=ET.text.toString()
        requestAPI()
        }
        Fullimage.setOnClickListener{
            closeImg()
        }
    }

    private fun requestAPI(){
        Log.d("apicall","api")
        CoroutineScope(IO).launch {

            data = async {
                fetchData()


            }.await()
            if(data.isNotEmpty()){
                updateData(data)
                Log.d("update","method")

        }
    }}

    private fun fetchData(): String{
        var response = ""
        try {
            response = URL("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=0e54dbd3c06aa3b2914fd52c53d35d92&tags=$tag&format=json&nojsoncallback=1")
                .readText(Charsets.UTF_8)
            Log.d("read","url")
        }catch (e: Exception){
            println("Error: $e")
        }
        return response
    }
     suspend fun updateData(photo:String) {
         withContext(Main) {
             val jsonObj = JSONObject(photo)
             val photosObj = jsonObj.getJSONObject("photos")
             val photoArray = photosObj.getJSONArray("photo")
             var i = 0
             try {
                 while (photoArray.getJSONObject(i) != null) {
                     Log.d("update", "update")
                     var obj = photoArray.getJSONObject(i)
                     var farm = obj.getString("farm")
                     var server = obj.getString("server")
                     var id = obj.getString("id")
                     var secret = obj.getString("secret")
                     var title = obj.getString("title")

                     var url = "http://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
                     photos.add(photoClass(title,url))
                     i++
                     Log.d("update", "upddoneate")
                 }
             } catch (e: Exception) {
                 Log.d("donephoto", "donephoto")
             }

             Rc.adapter?.notifyDataSetChanged()

         }
     }
    fun fullImage(url:String){
        Glide.with(this).load(url).into(Fullimage)
        Fullimage.isVisible = true
        Rc.isVisible = false
        b.isVisible = false
        ET.isVisible=false
    }
     fun closeImg(){
        Fullimage.isVisible = false
        Rc.isVisible = true
        b.isVisible = true
         ET.isVisible=true
    }
    }

