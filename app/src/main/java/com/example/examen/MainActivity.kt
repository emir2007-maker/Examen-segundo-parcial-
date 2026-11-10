package com.example.examen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.view.ViewGroup
import android.graphics.Color as AndroidColor
import android.graphics.drawable.GradientDrawable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.os.Handler
import android.os.Looper
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

private val Morado = Color(0xFF6558C8)
private val Fondo = Color(0xFFF6F5FA)
private val Texto = Color(0xFF29263D)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PantallaPerfil()
            }
        }
    }
}

@Composable
fun PantallaPerfil() {

    val context = LocalContext.current

    var nombre by remember {
        mutableStateOf("Usuario")
    }

    var correo by remember {
        mutableStateOf("usuario@gmail.com")
    }

    var descripcion by remember {
        mutableStateOf(
            "Hola, soy una persona creativa, " +
                    "me gusta aprender cosas nuevas, conocer lugares " +
                    "y compartir mis intereses."
        )
    }

    var mostrarEdicion by remember {
        mutableStateOf(false)
    }

    var nombreTemporal by remember {
        mutableStateOf("")
    }

    var correoTemporal by remember {
        mutableStateOf("")
    }

    var descripcionTemporal by remember {
        mutableStateOf("")
    }

    var fotoPerfil by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var imagenPortada by remember {
        mutableStateOf<Bitmap?>(null)
    }

    // SELECTOR DE FOTO DE PERFIL
    val selectorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null) {

            val bitmap = runCatching {
                context.contentResolver
                    .openInputStream(uri)
                    ?.use {
                        BitmapFactory.decodeStream(it)
                    }
            }.getOrNull()

            fotoPerfil = bitmap
        }
    }

    // SELECTOR DE PORTADA
    val selectorPortada = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null) {

            val bitmap = runCatching {
                context.contentResolver
                    .openInputStream(uri)
                    ?.use {
                        BitmapFactory.decodeStream(it)
                    }
            }.getOrNull()

            imagenPortada = bitmap
        }
    }

    // INTERESES
    val etiquetas = remember {
        mutableStateListOf(
            "Música",
            "Deportes",
            "Tecnología",
            "Viajes",
            "Fotografía",
            "Programación",
            "Videojuegos",
            "Arte",
            "Cine",
            "Lectura"
        )
    }

    var mostrarDialogo by remember {
        mutableStateOf(false)
    }

    var nuevaEtiqueta by remember {
        mutableStateOf("")
    }

    Scaffold(
        containerColor = Fondo,

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    nuevaEtiqueta = ""
                    mostrarDialogo = true
                },
                containerColor = Morado,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text(
                    text = "+",
                    fontSize = 32.sp
                )
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {

            // PORTADA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Morado)
                    .clickable {
                        selectorPortada.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {

                imagenPortada?.let { imagen ->

                    Image(
                        bitmap = imagen.asImageBitmap(),
                        contentDescription = "Imagen de portada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Black.copy(alpha = 0.30f)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "MI PERFIL",
                        fontSize = 27.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "📷 Cambiar portada",
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // FOTO Y DATOS
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(95.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2DDF8))
                        .clickable {
                            selectorFoto.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {

                    if (fotoPerfil != null) {

                        Image(
                            bitmap = fotoPerfil!!.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Text(
                            text = nombre.firstOrNull()
                                ?.uppercase() ?: "U",
                            fontSize = 40.sp,
                            color = Morado,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = nombre,
                        fontSize = 22.sp,
                        color = Texto,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = correo,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "Toca la foto para cambiarla",
                        fontSize = 11.sp,
                        color = Morado
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // EDITAR PERFIL
            OutlinedButton(
                onClick = {
                    nombreTemporal = nombre
                    correoTemporal = correo
                    descripcionTemporal = descripcion
                    mostrarEdicion = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    "Editar mi perfil",
                    color = Morado
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // ABOUT
            Text(
                text = "About",
                fontSize = 21.sp,
                color = Texto,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = descripcion,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(23.dp))

            // INTERESES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Mis intereses",
                    fontSize = 21.sp,
                    color = Texto,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Mantén tus intereses actualizados",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = etiquetas,
                    key = { it }
                ) { etiqueta ->

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFFE6E1FA))
                            .padding(
                                start = 13.dp,
                                end = 7.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = etiqueta,
                            fontSize = 13.sp,
                            color = Morado,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.White.copy(alpha = 0.8f)
                                )
                                .clickable {
                                    etiquetas.remove(etiqueta)
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "×",
                                fontSize = 16.sp,
                                color = Morado,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // UBICACION
            Text(
                text = "Mi ubicación",
                fontSize = 21.sp,
                color = Texto,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TARJETA DE UBICACION
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "📍",
                    fontSize = 27.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        text = "Universidad Politécnica de Pachuca",
                        fontSize = 15.sp,
                        color = Texto,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Zempoala, Hidalgo",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "Mi universidad",
                        fontSize = 11.sp,
                        color = Morado
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // IMAGEN DEL MAPA
            MapaReal()

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // DIALOGO EDITAR PERFIL
    if (mostrarEdicion) {

        AlertDialog(

            onDismissRequest = {
                mostrarEdicion = false
            },

            title = {
                Text(
                    "Editar mi perfil",
                    color = Texto,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = nombreTemporal,
                        onValueChange = {
                            nombreTemporal = it
                        },
                        label = {
                            Text("Nombre")
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = correoTemporal,
                        onValueChange = {
                            correoTemporal = it
                        },
                        label = {
                            Text("Correo electrónico")
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = descripcionTemporal,
                        onValueChange = {
                            descripcionTemporal = it
                        },
                        label = {
                            Text("Descripción About")
                        },
                        minLines = 3
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        if (nombreTemporal.isNotBlank()) {
                            nombre = nombreTemporal.trim()
                        }

                        if (
                            android.util.Patterns.EMAIL_ADDRESS
                                .matcher(correoTemporal.trim())
                                .matches()
                        ) {
                            correo = correoTemporal.trim()
                        }

                        descripcion = descripcionTemporal.trim()

                        mostrarEdicion = false
                    }
                ) {

                    Text(
                        "Guardar cambios",
                        color = Morado
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarEdicion = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // DIALOGO AGREGAR INTERESES
    if (mostrarDialogo) {

        AlertDialog(

            onDismissRequest = {
                mostrarDialogo = false
            },

            title = {
                Text("Agregar interés")
            },

            text = {

                OutlinedTextField(
                    value = nuevaEtiqueta,
                    onValueChange = {
                        nuevaEtiqueta = it
                    },
                    label = {
                        Text("Nombre del interés")
                    },
                    singleLine = true
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val texto = nuevaEtiqueta.trim()

                        if (
                            texto.isNotEmpty() &&
                            etiquetas.none {
                                it.equals(texto, ignoreCase = true)
                            }
                        ) {
                            etiquetas.add(texto)
                        }

                        mostrarDialogo = false
                    }
                ) {

                    Text(
                        "Agregar",
                        color = Morado
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDialogo = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ==========================================
// IMAGEN DE UBICACION DE LA UNIVERSIDAD UPP
// ==========================================

@Composable
fun MapaReal() {

    var imagenMapa by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var errorMapa by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {

            var bitmap: Bitmap? = null

            try {

                val url = URL(
                    "https://staticmap.openstreetmap.de/staticmap.php" +
                            "?center=19.98007,-98.6853" +
                            "&zoom=16" +
                            "&size=800x450" +
                            "&maptype=mapnik" +
                            "&markers=19.98007,-98.6853,red-pushpin"
                )

                val connection =
                    url.openConnection() as HttpURLConnection

                connection.connectTimeout = 15000
                connection.readTimeout = 20000
                connection.requestMethod = "GET"
                connection.setRequestProperty(
                    "User-Agent",
                    "AndroidUPPProfile/1.0"
                )

                connection.connect()

                if (connection.responseCode == 200) {

                    bitmap = connection.inputStream.use {
                        BitmapFactory.decodeStream(it)
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {

                android.util.Log.e(
                    "MAPA_UPP",
                    "Error al descargar imagen: ${e.message}"
                )
            }

            Handler(Looper.getMainLooper()).post {

                imagenMapa = bitmap
                errorMapa = bitmap == null
                cargando = false
            }

            executor.shutdown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(Color(0xFFEAE7F5)),
            contentAlignment = Alignment.Center
        ) {

            when {

                imagenMapa != null -> {

                    Image(
                        bitmap = imagenMapa!!.asImageBitmap(),
                        contentDescription =
                            "Mapa de la Universidad Politécnica de Pachuca",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                cargando -> {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator(
                            color = Morado
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = "Cargando ubicación de la UPP...",
                            color = Texto,
                            fontSize = 13.sp
                        )
                    }
                }

                errorMapa -> {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "📍",
                            fontSize = 40.sp
                        )

                        Text(
                            text = "Universidad Politécnica de Pachuca",
                            color = Texto,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Zempoala, Hidalgo",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )

                        Text(
                            text = "No se pudo cargar la imagen del mapa",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "📍",
                fontSize = 22.sp
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column {

                Text(
                    text = "Universidad Politécnica de Pachuca",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Texto
                )

                Text(
                    text = "Ex-Hacienda de Santa Bárbara, Zempoala",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}