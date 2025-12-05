package com.example.proyectohuertohogar.model

import com.google.gson.annotations.SerializedName

data class ProductApiModel(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: Int,
    @SerializedName("stock") val stock: Int,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("imagen") val imagenUrl: String?,
    @SerializedName("categoria") val categoria: String?
)

fun ProductApiModel.toDomain(): Product {

    // Mapa de datos enriquecidos (Imagen, Descripción, Categoría)
    val productDetails = mapOf(
        "Manzanas Fuji" to Triple(
            "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=500",
            "Crujientes y dulces, perfectas para snacks.",
            "Frutas"
        ),
        "Naranjas Valencia" to Triple(
            "https://images.unsplash.com/photo-1547514701-42782101795e?w=500",
            "Jugosas y llenas de vitamina C.",
            "Frutas"
        ),
        "Plátanos Cavendish" to Triple(
            "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=500",
            "Energía natural rica en potasio.",
            "Frutas"
        ),
        "Zanahorias Orgánicas" to Triple(
            "https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?w=500",
            "Cultivadas sin pesticidas, dulces y frescas.",
            "Verduras"
        ),
        "Espinacas Frescas" to Triple(
            "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=500",
            "Hojas tiernas listas para ensaladas.",
            "Verduras"
        ),
        "Pimientos Tricolores" to Triple(
            "https://images.unsplash.com/photo-1563565375-f3fdf5dbc240?w=500",
            "Variedad de colores para tus comidas.",
            "Verduras"
        ),
        "Miel de Abejas" to Triple(
            "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=500",
            "100% pura de floración nativa.",
            "Despensa"
        ),
        "Quinua Real" to Triple(
            "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=500",
            "Superalimento andino lavado.",
            "Despensa"
        ),
        "Leche Entera" to Triple(
            "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=500",
            "Fresca de vaca, pasteurizada.",
            "Lácteos"
        ),
        "Queso de Cabra" to Triple(
            "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=500",
            "Artesanal con especias finas.",
            "Lácteos"
        )
    )

    val details = productDetails[this.nombre]

    return Product(
        id = this.id.toString(),
        name = this.nombre,
        price = this.precio,
        unit = "unidad",
        stock = this.stock,
        // Usamos nuestros datos bonitos si existen, si no, los de la API
        description = details?.second ?: this.descripcion ?: "Producto fresco de calidad.",
        category = details?.third ?: this.categoria ?: "General",
        imageUrl = details?.first ?: this.imagenUrl ?: ""
    )
}