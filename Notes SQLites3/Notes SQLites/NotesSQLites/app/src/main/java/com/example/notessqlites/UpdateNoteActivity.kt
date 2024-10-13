package com.example.notessqlites

import Note
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlites.databinding.ActivityAddNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private val CAMERA_REQUEST_CODE = 1001
    private val GALLERY_REQUEST_CODE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        val noteId = intent.getIntExtra("NOTE_ID", -1)
        if (noteId != -1) {
            val note = db.getNoteById(noteId, this)
            if (note != null) {
                binding.titleEditText.setText(note.title)
                binding.contentEditText.setText(note.content)
                note.image?.let { image ->
                    val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    binding.cameraButton.setImageBitmap(bitmap) // imageView는 XML에서 정의한 ImageView의 ID
                }
            } else {
                // 노트를 찾을 수 없을 경우 처리
            }
        }


        binding.cameraButton.setOnClickListener {
            val options = arrayOf("카메라", "갤러리")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Image Source")
            builder.setItems(options) { dialog, which ->
                if (which == 0) {
                    // 카메라 선택
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                } else {
                    // 갤러리 선택
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                }
            }
            builder.show()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val note = Note(noteId, title, content) // noteId 사용
            db.updateNoteWithImage(note) // 업데이트 메소드 사용
            finish()
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
        }
    }

    // 이미지를 선택한 결과를 처리하는 부분
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    binding.cameraButton.setImageBitmap(photo)
                    // 이 이미지를 데이터베이스에 저장하는 코드 추가
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedImage = data?.data
                    binding.cameraButton.setImageURI(selectedImage)
                    // 이 URI 또는 Bitmap을 데이터베이스에 저장하는 코드 추가
                }
            }
        }
    }
}
