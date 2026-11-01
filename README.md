# Anclaje (Fabric, Minecraft 26.3)

- **Caña**: con algo enganchado (mob u objeto), al recoger la caña tú te impulsas
  hacia él; la entidad no se mueve.
- **Murciélago**: los puestos con huevo de aparición quedan estáticos (sin IA, sin
  gravedad, sin despawn). Los naturales de cuevas no cambian.

## Compilar con GitHub
1. Crea un repositorio y sube todo este contenido (incluida la carpeta `.github`).
2. En la pestaña **Actions** corre solo el workflow `build` con cada push.
3. Al terminar, entra a la ejecución y descarga el artefacto **anclaje-jar**.
4. Para una Release con el .jar: `git tag v1.0.0 && git push origin v1.0.0`.

## Instalar
Pon el .jar en `mods/` con Fabric Loader >= 0.19.5, Fabric API 0.161.0+26.3 y Java 25.
