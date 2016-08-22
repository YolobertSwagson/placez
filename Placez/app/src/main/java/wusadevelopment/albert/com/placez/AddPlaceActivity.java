package wusadevelopment.albert.com.placez;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AddPlaceActivity extends AppCompatActivity {

    private ImageButton AddPlaceAddPictureGalleryBtn;
    private ImageButton AddPlaceAddPictureTakePictureBtn;

    private ImageView AddPlaceImagePreview;

    private Uri picUri;

    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        this.AddPlaceAddPictureGalleryBtn = (ImageButton) findViewById(R.id.AddPlaceAddPictureGalleryBtn);
        this.AddPlaceAddPictureTakePictureBtn = (ImageButton) findViewById(R.id.AddPlaceAddPictureTakePictureBtn);

        this.AddPlaceAddPictureGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        this.AddPlaceAddPictureTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        this.AddPlaceImagePreview = (ImageView) findViewById(R.id.AddPlaceImagePreview);

        Spinner spinner = (Spinner) findViewById(R.id.AddPlaceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    protected void pickImage(){
        try {
            Intent cropIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            cropIntent.setType("image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);

            File f = new File(Environment.getExternalStorageDirectory(), "/tmp_img.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            Uri tmpImageUri = Uri.fromFile(f);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpImageUri);

            startActivityForResult(cropIntent, 1);

        }
        catch (ActivityNotFoundException anfe) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void takePicture(){
        try {
            //use standard intent to capture an image
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //we will handle the returned data in onActivityResult
            startActivityForResult(captureIntent, 2);
        }catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if(requestCode == 1){
            String filePath = Environment.getExternalStorageDirectory()
                    + "/tmp_img.jpg";

            Bitmap thumbnail = BitmapFactory.decodeFile(filePath);
            File file = new File(filePath);
            file.delete();
            AddPlaceImagePreview.setImageBitmap(thumbnail);
            AddPlaceImagePreview.setVisibility(View.VISIBLE);

            //Beispiel für Bitmap zu String und zurück
            encodedImage = encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);
            Bitmap myBitmapAgain = decodeBase64(encodedImage);

        }else if(requestCode == 2){
            picUri = data.getData();
            performCrop();
        }
    }

    protected void performCrop(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            cropIntent.putExtra("return-data", true);
            File f = new File(Environment.getExternalStorageDirectory(), "/tmp_img.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            Uri tmpImageUri = Uri.fromFile(f);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpImageUri);
            startActivityForResult(cropIntent, 1);
        }
        catch(ActivityNotFoundException anfe){
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
