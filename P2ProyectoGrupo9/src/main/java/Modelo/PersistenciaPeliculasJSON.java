package Modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.Objects;

/**
 * Persistencia en JSONL (una película por línea) y ordenamiento por Mezcla Natural.
 * Mantiene arquitectura MVC ubicándose en el paquete Modelo como servicio estático.
 */
public final class PersistenciaPeliculasJSON {

    public enum Campo { TITULO, DIRECTOR, ANIO, GENERO }

    private static final Gson GSON = new GsonBuilder().create();
    private static final Path DATA_PATH = Paths.get(System.getProperty("user.dir"), "peliculas.jsonl");

    private PersistenciaPeliculasJSON() {}

    public static Path getRutaArchivo() { return DATA_PATH; }

    /** Guarda todo el catálogo a un archivo JSONL. */
    public static void guardar(ListaEnlazada lista) {
        Objects.requireNonNull(lista, "lista");
        try {
            Files.createDirectories(DATA_PATH.getParent());
        } catch (IOException ignored) {}
        try (BufferedWriter bw = Files.newBufferedWriter(DATA_PATH, StandardCharsets.UTF_8)) {
            for (Pelicula p : lista.obtenerTodasLasPeliculas()) {
                bw.write(GSON.toJson(p));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error guardando peliculas en JSONL", e);
        }
    }

    /** Carga desde archivo JSONL en la lista (limpia y añade al final en el orden del archivo). */
    public static void cargarEn(ListaEnlazada lista) {
        Objects.requireNonNull(lista, "lista");
        if (!Files.exists(DATA_PATH)) return;
        lista.limpiar();
        try (BufferedReader br = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                Pelicula p = GSON.fromJson(line, Pelicula.class);
                if (p != null) {
                    lista.insertarAlFinal(p);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error cargando peliculas desde JSONL", e);
        }
    }

    /** Ordena el archivo por Mezcla Natural según campo y sentido. */
    public static void ordenarPorCampo(Campo campo, boolean ascendente) {
        if (!Files.exists(DATA_PATH)) return;
        try {
            mezclaNatural(DATA_PATH, comparador(campo, ascendente));
        } catch (IOException e) {
            throw new UncheckedIOException("Error ordenando archivo por mezcla natural", e);
        }
    }

    /**
     * Obtiene un comparador para el campo y dirección especificados.
     * Público para permitir ordenamiento en memoria.
     */
    public static Comparator<Pelicula> obtenerComparador(Campo campo, boolean ascendente) {
        return comparador(campo, ascendente);
    }

    /**
     * Limpia archivos temporales de Mezcla Natural si existen.
     */
    public static void limpiarTemporales() {
        try {
            Files.deleteIfExists(DATA_PATH.resolveSibling("tmp_a.jsonl"));
            Files.deleteIfExists(DATA_PATH.resolveSibling("tmp_b.jsonl"));
            Files.deleteIfExists(DATA_PATH.resolveSibling("tmp_out.jsonl"));
        } catch (Exception ignored) {}
    }

    private static Comparator<Pelicula> comparador(Campo campo, boolean ascendente) {
        Comparator<Pelicula> base;
        switch (campo) {
            case TITULO:
                base = Comparator.comparing(p -> normalizar(((Pelicula)p).getTitulo()), String::compareTo);
                break;
            case DIRECTOR:
                base = Comparator.comparing(p -> normalizar(((Pelicula)p).getDirector()), String::compareTo);
                break;
            case GENERO:
                base = Comparator.comparing(p -> normalizar(((Pelicula)p).getGenero()), String::compareTo);
                break;
            case ANIO:
            default:
                base = Comparator.comparingInt(Pelicula::getAnio);
        }
        return ascendente ? base : base.reversed();
    }

    private static String normalizar(String texto) {
        if (texto == null) return "";
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalizado.toLowerCase().trim();
    }

    // ================== Mezcla Natural sobre JSONL ==================

    private static void mezclaNatural(Path origen, Comparator<Pelicula> comparador) throws IOException {
        Path archivoA = origen.resolveSibling("tmp_a.jsonl");
        Path archivoB = origen.resolveSibling("tmp_b.jsonl");
        Path archivoSalida = origen.resolveSibling("tmp_out.jsonl");

        while (true) {
            int corridas = dividirEnCorridas(origen, archivoA, archivoB, comparador);
            if (corridas <= 1) {
                // Ya está ordenado en origen
                Files.deleteIfExists(archivoA);
                Files.deleteIfExists(archivoB);
                Files.deleteIfExists(archivoSalida);
                return;
            }
            fusionarCorridas(archivoA, archivoB, archivoSalida, comparador);
            Files.deleteIfExists(origen);
            Files.move(archivoSalida, origen, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }
    }

    private static int dividirEnCorridas(Path origen, Path archivoA, Path archivoB, Comparator<Pelicula> comparador) throws IOException {
        try (BufferedReader lector = Files.newBufferedReader(origen, StandardCharsets.UTF_8);
             BufferedWriter escritorA = Files.newBufferedWriter(archivoA, StandardCharsets.UTF_8);
             BufferedWriter escritorB = Files.newBufferedWriter(archivoB, StandardCharsets.UTF_8)) {

            BufferedWriter escritorActual = escritorA;
            boolean haciaA = true;
            int corridas = 0;
            String linea;
            Pelicula anterior = null;

            while ((linea = lector.readLine()) != null) {
                if (linea.isBlank()) continue;
                Pelicula actual = GSON.fromJson(linea, Pelicula.class);
                if (actual == null) continue;
                if (anterior == null || comparador.compare(anterior, actual) <= 0) {
                    // misma corrida
                    escritorActual.write(GSON.toJson(actual));
                    escritorActual.newLine();
                } else {
                    // Nueva corrida: alternar archivo
                    haciaA = !haciaA;
                    escritorActual = haciaA ? escritorA : escritorB;
                    corridas++;
                    escritorActual.write(GSON.toJson(actual));
                    escritorActual.newLine();
                }
                anterior = actual;
            }
            if (anterior != null) corridas++; // contar la primera corrida
            return corridas;
        }
    }

    private static void fusionarCorridas(Path archivoA, Path archivoB, Path archivoSalida, Comparator<Pelicula> comparador) throws IOException {
        try (BufferedReader lectorA = Files.newBufferedReader(archivoA, StandardCharsets.UTF_8);
             BufferedReader lectorB = Files.newBufferedReader(archivoB, StandardCharsets.UTF_8);
             BufferedWriter escritor = Files.newBufferedWriter(archivoSalida, StandardCharsets.UTF_8)) {

            Pelicula peliculaA = leer(lectorA);
            Pelicula peliculaB = leer(lectorB);
            Pelicula anteriorA = null, anteriorB = null;

            while (peliculaA != null || peliculaB != null) {
                // fusionar una corrida a la vez
                while (peliculaA != null && peliculaB != null) {
                    boolean finA = anteriorA != null && comparador.compare(anteriorA, peliculaA) > 0;
                    boolean finB = anteriorB != null && comparador.compare(anteriorB, peliculaB) > 0;
                    if (finA && finB) break;
                    if (finA) { // drenar corrida B
                        while (peliculaB != null) {
                            if (anteriorB != null && comparador.compare(anteriorB, peliculaB) > 0) break;
                            escribir(escritor, peliculaB);
                            anteriorB = peliculaB;
                            peliculaB = leer(lectorB);
                        }
                        break;
                    }
                    if (finB) { // drenar corrida A
                        while (peliculaA != null) {
                            if (anteriorA != null && comparador.compare(anteriorA, peliculaA) > 0) break;
                            escribir(escritor, peliculaA);
                            anteriorA = peliculaA;
                            peliculaA = leer(lectorA);
                        }
                        break;
                    }
                    if (comparador.compare(peliculaA, peliculaB) <= 0) {
                        escribir(escritor, peliculaA);
                        anteriorA = peliculaA;
                        peliculaA = leer(lectorA);
                    } else {
                        escribir(escritor, peliculaB);
                        anteriorB = peliculaB;
                        peliculaB = leer(lectorB);
                    }
                }
                // drenar resto de corrida A
                while (peliculaA != null) {
                    if (anteriorA != null && comparador.compare(anteriorA, peliculaA) > 0) break;
                    escribir(escritor, peliculaA);
                    anteriorA = peliculaA;
                    peliculaA = leer(lectorA);
                }
                // drenar resto de corrida B
                while (peliculaB != null) {
                    if (anteriorB != null && comparador.compare(anteriorB, peliculaB) > 0) break;
                    escribir(escritor, peliculaB);
                    anteriorB = peliculaB;
                    peliculaB = leer(lectorB);
                }
            }
        }
    }

    private static Pelicula leer(BufferedReader lector) throws IOException {
        String linea;
        while ((linea = lector.readLine()) != null) {
            if (linea.isBlank()) continue;
            return GSON.fromJson(linea, Pelicula.class);
        }
        return null;
    }

    private static void escribir(BufferedWriter escritor, Pelicula pelicula) throws IOException {
        escritor.write(GSON.toJson(pelicula));
        escritor.newLine();
    }
}
