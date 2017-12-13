package mx.com.develop.intimex;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class RegistrarProducto extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1;
    private File url_imagen;
    private int contador = 1;

    String mPermisoWriteStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    String mPermisoReadStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    public static final int REQUEST_CODE = 1000;

    ImageView iv_foto;
    EditText et_nombre_producto,
            et_marca_producto,
            et_precio_compra,
            et_precio_venta,
            et_cantidad_stock;
    Button btn_tomar_foto, btn_registrar_producto;
    Producto mProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);
        inicializarComponentesUI();
        inicializarSetOnClickListener();
    }

    private void inicializarComponentesUI() {
        iv_foto = findViewById(R.id.iv_foto);
        et_nombre_producto = findViewById(R.id.et_nombre_producto);
        et_marca_producto = findViewById(R.id.et_marca_producto);
        et_precio_compra = findViewById(R.id.et_precio_compra);
        et_precio_venta = findViewById(R.id.et_precio_venta);
        et_cantidad_stock = findViewById(R.id.et_cantidad_stock);
        btn_tomar_foto = findViewById(R.id.btn_tomar_foto);
        btn_registrar_producto = findViewById(R.id.btn_registrar_producto);
    }

    private void inicializarSetOnClickListener() {
        btn_tomar_foto.setOnClickListener(this);
        btn_registrar_producto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_tomar_foto:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                    checarPermisos();
                else capturarFoto();

                break;

            case R.id.btn_registrar_producto:
                guardarProducto();
                enviarDetalleProducto();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == GRANTED) {
                capturarFoto();
            } else {
                Toast.makeText(this, "EL permiso es esencial para poder continuar", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void capturarFoto() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = null;

        if (isExternalStorageWritable()) {
            String nombreDirectorioPublico = "IntiMex";
            imagesFolder = crearDirectorioPublico(nombreDirectorioPublico);
        }


        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        @SuppressLint("DefaultLocale") String nombrefoto = String.format("producto_%d.jpg", contador);
        contador = contador + 1;
        //Añadimos el nombre de la imágen
        url_imagen = new File(imagesFolder, nombrefoto);
        Uri uriSavedImage = Uri.fromFile(url_imagen);
        //Le decimos al Intent que queremos grabar la imágen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        //Lanzamos la aplicación de la camara con retorno (forResult)
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprobamos que la foto se a realizado
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //Creamos un bitmap con la imágen recientemente almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(url_imagen.getAbsolutePath());
            //Añadimos el bitmap al imageView para mostrarlo en la pantalla
            iv_foto.setImageBitmap(bMap);
        }
    }

    private void guardarProducto() {
        String nombre = et_nombre_producto.getText().toString().trim();
        String marca = et_marca_producto.getText().toString().trim();
        float precio_compra = Float.valueOf(et_precio_compra.getText().toString().trim());
        float precio_venta = Float.valueOf(et_precio_venta.getText().toString().trim());
        int cantidad = Integer.valueOf(et_cantidad_stock.getText().toString().trim());
        mProducto = new Producto(url_imagen, nombre, marca, precio_compra, precio_venta, cantidad);
    }

    private void enviarDetalleProducto() {
        Intent intentDetalle = new Intent(RegistrarProducto.this, DetalleProducto.class);
        intentDetalle.putExtra("KEY_PRODUCTO", mProducto);
        startActivity(intentDetalle);
    }

    private void checarPermisos() {
        if (checkPermission(mPermisoWriteStorage) != GRANTED && checkPermission(mPermisoReadStorage) != GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{mPermisoWriteStorage,}, REQUEST_CODE);
        else capturarFoto();
    }

    private int checkPermission(String namePermisssion) {
        return ActivityCompat.checkSelfPermission(this, namePermisssion);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public File crearDirectorioPrivado(String nombreDirectorio) {
        //Crear directorio privado en la carpeta Pictures.
        File directorio = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.exists())
            directorio.mkdirs();

        return directorio;
    }

    public File crearDirectorioPublico(String nombreDirectorio) {
        //Crear directorio público en la carpeta Pictures.
        File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.exists())
            directorio.mkdirs();

        return directorio;
    }

    public File crearCarpetaAlmacenamientoInterno(String nombreDirectorio) {
        //Crear directorio en la memoria interna.
        File directorio = new File(getFilesDir(), nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.exists())
            directorio.mkdirs();
        return directorio;
    }
}
