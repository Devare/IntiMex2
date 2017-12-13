package mx.com.develop.intimex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetalleProducto extends AppCompatActivity {

    ImageView iv_foto_detalle;
    TextView tv_detalle_producto;
    Producto mProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        inicializarComponentesUI();
        obtenerDatos();
        mostrarDatos();
    }

    private void mostrarDatos() {
        Bitmap bMap = BitmapFactory.decodeFile(mProducto.getFoto().getAbsolutePath());
        iv_foto_detalle.setImageBitmap(bMap);
        String detalleProducto="Nombre: "+ mProducto.getNombre() + "\n"+
                "Marca: "+mProducto.getMarca()  +"\n"+
                "Precio de Compra $ "+mProducto.getPrecioCompra() +"\n"+
                "Precio de venta $ "+mProducto.getPrecioVenta() +"\n"+
                "Stock: "+mProducto.getCantidad();
        tv_detalle_producto.setText(detalleProducto);
    }

    private void obtenerDatos() {
        Intent mIntent=getIntent();
        mProducto=(Producto) mIntent.getSerializableExtra("KEY_PRODUCTO");
    }

    private void inicializarComponentesUI() {
        iv_foto_detalle=(ImageView) findViewById(R.id.iv_foto_detalle);
        tv_detalle_producto=(TextView) findViewById(R.id.tv_detalle_producto);
    }
}
