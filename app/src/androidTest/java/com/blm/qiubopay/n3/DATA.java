package com.blm.qiubopay.n3;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.platform.app.InstrumentationRegistry;

import java.io.InputStream;
import java.util.Properties;

/**
 * Configuración de datos utilizados de forma general
 *
 */
public class DATA {

    /**
     *
     */
    public static final  Integer TIEMPO_ACCION = getPropertyInt("TIEMPO_ACCION");
    public static final  Integer TIEMPO_INICIO = getPropertyInt("TIEMPO_INICIO");
    public static final  Integer TIEMPO_FIN = getPropertyInt("TIEMPO_FIN");

    /**
     * Datos otros
     */
    public static final  String PIN_DIFERENTE = getProperty("PIN_DIFERENTE");
    public static final  String PIN_INCOMPLETO = getProperty("PIN_INCOMPLETO");
    public static final  String CORREO_DIFERENTE = getProperty("CORREO_DIFERENTE");
    public static final  String CORREO_INVALIDO = getProperty("CORREO_INVALIDO");
    public static final  String CONTRASENA_DIFERENTE = getProperty("CONTRASENA_DIFERENTE");
    public static final  String CONTRASENA_CARACTERES = getProperty("CONTRASENA_CARACTERES");
    public static final  String CONTRASENA_TEMPORAL = getProperty("CONTRASENA_TEMPORAL");
    public static final  String OTP_DIFERENTE = getProperty("OTP_DIFERENTE");
    public static final  String OTP = getProperty("OTP");

    /**
     * Datos de usuario
     */
    public static class USUARIO {

        public static final  String CORREO_ELECTRONICO = getProperty("CORREO_ELECTRONICO");
        public static final  String CONTRASENA = getProperty("CONTRASENA");
        public static final  String PIN = getProperty("PIN");

        public static final  String NOMBRE = getProperty("NOMBRE");
        public static final  String APELLIDO_PATERNO = getProperty("APELLIDO_PATERNO");
        public static final  String APELLIDO_MATERNO = getProperty("APELLIDO_MATERNO");
        public static final  String NUMERO_CELULAR = getProperty("NUMERO_CELULAR");

        public static final  String COLABORADOR = getProperty("COLABORADOR");
        public static final  String CODIGO_BIMBO = getProperty("CODIGO_BIMBO");

        public static final  String CANAL = getProperty("CANAL");

        public static final  String CALLE = getProperty("CALLE");
        public static final  String ENTRE_CALLES = getProperty("ENTRE_CALLES");
        public static final  String REFERENCIAS = getProperty("REFERENCIAS");
        public static final  String EXTERIOR = getProperty("EXTERIOR");
        public static final  String CODIGO_POSTAL = getProperty("CODIGO_POSTAL");
        public static final  String ESTADO = getProperty("ESTADO");
        public static final  String MUNICIPIO = getProperty("MUNICIPIO");
        public static final  String POBLACION = getProperty("POBLACION");

    }

    /**
     * Datos de negocio
     */
    public static class NEGOCIO {

        public static final  String NOMBRE = getProperty("NEGOCIO_NOMBRE");
        public static final  String CALLE = getProperty("NEGOCIO_CALLE");
        public static final  String EXTERIOR = getProperty("NEGOCIO_EXTERIOR");
        public static final  String CODIGO_POSTAL = getProperty("NEGOCIO_CODIGO_POSTAL");
        public static final  String ESTADO = getProperty("NEGOCIO_ESTADO");
        public static final  String MUNICIPIO = getProperty("NEGOCIO_MUNICIPIO");
        public static final  String POBLACION = getProperty("NEGOCIO_POBLACION");

    }

    /**
     * Datos deposito
     */
    public static class DEPOSITO {

        public static class BANAMEX {
            public static final String MONTO = getProperty("BANAMEX_MONTO");
            public static final String REFERENCIA = getProperty("BANAMEX_REFERENCIA");
            public static final String NUMERO_SUCURSAL = getProperty("BANAMEX_NUMERO_SUCURSAL");
            public static final String FECHA_TRANSACCION = getProperty("BANAMEX_FECHA_TRANSACCION");
        }

        public static class BBVA {
            public static final String MONTO = getProperty("BBVA_MONTO");
            public static final String REFERENCIA = getProperty("BBVA_REFERENCIA");
            public static final String NUMERO_SUCURSAL = getProperty("BBVA_NUMERO_SUCURSAL");
            public static final String FECHA_TRANSACCION = getProperty("BBVA_FECHA_TRANSACCION");
        }

        public static class SANTANDER {
            public static final String MONTO = getProperty("SANTANDER_MONTO");
            public static final String REFERENCIA = getProperty("SANTANDER_REFERENCIA");
            public static final String NUMERO_SUCURSAL = getProperty("SANTANDER_NUMERO_SUCURSAL");
            public static final String FECHA_TRANSACCION = getProperty("SANTANDER_FECHA_TRANSACCION");
        }

        public static class OLC {
            public static final String MONTO = getProperty("OLC_MONTO");
            public static final String MONTO_MENOR = getProperty("OLC_MONTO");
        }

        public static class TARJETA {
            public static final String MONTO = getProperty("TARJETA_MONTO");
            public static final String NOMBRE = getProperty("TARJETA_NOMBRE");
            public static final String NUMERO = getProperty("TARJETA_NUMERO");
            public static final String EXPIRACION = getProperty("TARJETA_EXPIRACION");
            public static final String CVV = getProperty("TARJETA_CVV");
        }
    }

    /**
     * Datos recargas
     */
    public static class RECARGAS {

        public static final String NUMERO_DIFERENTE = getProperty("NUMERO_DIFERENTE");

        public static final String NUMERO_TELCEL = getProperty("NUMERO_TELCEL");
        public static final String NUMERO_MOVISTAR = getProperty("NUMERO_MOVISTAR");
        public static final String NUMERO_ATT = getProperty("NUMERO_ATT");
        public static final String NUMERO_UNEFON = getProperty("NUMERO_UNEFON");
        public static final String NUMERO_FREEDOMPOP = getProperty("NUMERO_FREEDOMPOP");
        public static final String NUMERO_VIRGINMOBILE = getProperty("NUMERO_VIRGINMOBILE");
        public static final String NUMERO_FOBO = getProperty("NUMERO_FOBO");
        public static final String NUMERO_PILLOFON = getProperty("NUMERO_PILLOFON");
    }

    /**
     * Datos fiado
     */
    public static class FIADO {

        public static final String MONTO = getProperty("FIADO_MONTO");
        public static final String LIMITE_MONTO = getProperty("FIADO_LIMITE_MONTO");
        public static final String LIMITE_TIEMPO = getProperty("FIADO_LIMITE_TIEMPO");
        public static final String INTERESES = getProperty("FIADO_INTERESES");

        public static final String DETALLE = getProperty("FIADO_DETALLE");

        public static final String NOMBRE = getProperty("FIADO_NOMBRE");
        public static final String CORREO_ELECTRONICO = getProperty("FIADO_CORREO_ELECTRONICO");
        public static final  String NUMERO_CELULAR = getProperty("FIADO_NUMERO_CELULAR");
        public static final String BUSCAR = getProperty("FIADO_BUSCAR");
    }

    /**
     * Datos servicios
     */
    public static class SERVICIOS {

        public static final String NUMERO_IZZI = getProperty("NUMERO_IZZI");
        public static final String MONTO_IZZI = getProperty("MONTO_IZZI");

        public static final String NUMERO_MEGA_CABLE = getProperty("NUMERO_MEGA_CABLE");
        public static final String MONTO_MEGA_CABLE = getProperty("MONTO_MEGA_CABLE");

        public static final String MONTO_TELMEX = getProperty("MONTO_TELMEX");
        public static final String NUMERO_TELMEX = getProperty("NUMERO_TELMEX");
        public static final String NUMERO_TELMEX_VERIFICADOR = getProperty("NUMERO_TELMEX_VERIFICADOR");

        public static final String NUMERO_REFERENCIA_SKY = getProperty("NUMERO_REFERENCIA_SKY");
        public static final String MONTO_SKY = getProperty("MONTO_SKY");

        public static final String NUMERO_REFERENCIA_VETV = getProperty("NUMERO_REFERENCIA_VETV");
        public static final String MONTO_VETV = getProperty("MONTO_VETV");

        public static final String NUMERO_REFERENCIA_CFE = getProperty("NUMERO_REFERENCIA_CFE");

        public static final String NUMERO_CUPON_DISH = getProperty("NUMERO_CUPON_DISH");
        public static final String NUMERO_REFERENCIA_DISH = getProperty("NUMERO_REFERENCIA_DISH");

        public static final String NUMERO_REFERENCIA_TELEVIA = getProperty("NUMERO_REFERENCIA_TELEVIA");


        public static final String NUMERO_NATURGY1 = getProperty("NUMERO_NATURGY1");
        public static final String NUMERO_NATURGY2 = getProperty("NUMERO_NATURGY2");
        public static final String MONTO_NATURGY = getProperty("MONTO_NATURGY");

        public static final String NUMERO_PASE_URBANO = getProperty("NUMERO_PASE_URBANO");

        public static final String NUMERO_TOTALPLAY = getProperty("NUMERO_TOTALPLAY");
        public static final String MONTO_TOTALPLAY= getProperty("MONTO_TOTALPLAY");

        public static final String NUMERO_CEAQ = getProperty("NUMERO_CEAQ");
        public static final String MONTO_CEAQ= getProperty("MONTO_CEAQ");

        public static final String NUMERO_STAR_TV = getProperty("NUMERO_STAR_TV");

        public static final String NUMERO_VEOLIA = getProperty("NUMERO_VEOLIA");
        public static final String MONTO_VEOLIA = getProperty("MONTO_VEOLIA");

        public static final String NUMERO_BLUE_TELECOMM = getProperty("NUMERO_BLUE_TELECOMM");

        public static final String NUMERO_PLAN_ATT = getProperty("NUMERO_PLAN_ATT");

        public static final String NUMERO_PLAN_MOVISTAR = getProperty("NUMERO_PLAN_MOVISTAR");
        public static final String MONTO_PLAN_MOVISTAR = getProperty("MONTO_PLAN_MOVISTAR");

        public static final String NUMERO_SACMEX = getProperty("NUMERO_SACMEX");
        public static final String MONTO_SACMEX = getProperty("MONTO_SACMEX");

        public static final String NUMERO_GOB_CDMX = getProperty("NUMERO_GOB_CDMX");
        public static final String MONTO_GOB_CDMX = getProperty("MONTO_GOB_CDMX");

        public static final String NUMERO_GOB_EDOMEX = getProperty("NUMERO_GOB_EDOMEX");
        public static final String MONTO_GOB_EDOMEX = getProperty("MONTO_GOB_EDOMEX");

        public static final String NUMERO_AYDM = getProperty("NUMERO_AYDM");
        public static final String MONTO_AYDM = getProperty("MONTO_AYDM");

        public static final String NUMERO_SIAPA = getProperty("NUMERO_SIAPA");
        public static final String MONTO_SIAPA = getProperty("MONTO_SIAPA");

        public static final String NUMERO_OPDM = getProperty("NUMERO_OPDM");
        public static final String MONTO_OPDM = getProperty("MONTO_OPDM");

        public static final String NUMERO_CESPT = getProperty("NUMERO_CESPT");
        public static final String MONTO_CESPT = getProperty("MONTO_CESPT");
    }

    /**
     * Datos deposito
     */
    public static class COBROT {

        public static final String CLABE = getProperty("COBROT_CLABE");

    }

    /**
     * Datos deposito
     */
    public static class REPORTES {

        public static final String ITEM_RECARGAS = getProperty("ITEM_RECARGAS");
        public static final String ITEM_SERVICIOS = getProperty("ITEM_SERVICIOS");
        public static final String ITEM_PAGO_TARJETA = getProperty("ITEM_PAGO_TARJETA");
        public static final String ITEM_CARGOS_ABONOS = getProperty("ITEM_CARGOS_ABONOS");
        public static final String ITEM_TOTAL_PAGO_TARJETA = getProperty("ITEM_TOTAL_PAGO_TARJETA");

    }

    /**
     * Datos deposito
     */
    public static class CHAMBITAS {

        public static final String OPCION_1 = getProperty("OPCION_1");
        public static final String OPCION_2 = getProperty("OPCION_2");
        public static final String OPCION_3 = getProperty("OPCION_3");

        public static final String RETO_1 = getProperty("RETO_1");
        public static final String RETO_2 = getProperty("RETO_2");
        public static final String RETO_3 = getProperty("RETO_3");
        public static final String RETO_4 = getProperty("RETO_4");
        public static final String RETO_5 = getProperty("RETO_5");

        public static final String RESPUESTA_1 = getProperty("RESPUESTA_1");
        public static final String RESPUESTA_1_C = getProperty("RESPUESTA_1_C");
        public static final String RESPUESTA_2 = getProperty("RESPUESTA_2");
        public static final String RESPUESTA_3 = getProperty("RESPUESTA_3");
        public static final String RESPUESTA_4 = getProperty("RESPUESTA_4");
        public static final String RESPUESTA_5_1 = getProperty("RESPUESTA_5_1");
        public static final String RESPUESTA_5_2 = getProperty("RESPUESTA_5_2");
        public static final String RESPUESTA_5_3 = getProperty("RESPUESTA_5_3");
        public static final String RESPUESTA_5_4 = getProperty("RESPUESTA_5_4");
        public static final String RESPUESTA_5_5 = getProperty("RESPUESTA_5_5");
        public static final String RESPUESTA_5_6 = getProperty("RESPUESTA_5_6");
        public static final String RESPUESTA_5_7 = getProperty("RESPUESTA_5_7");
        public static final String RESPUESTA_5_MAYOR = getProperty("RESPUESTA_5_MAYOR");

        /**

         - Pregunta curiosa
            - Reto Pregunta abierta numérica QA
                12345

         - JG - IOS Test
            - Reto Pregunta abierta numérica QA
                12345

         - ¡Tu opinión nos importa!
            - Reto Encuesta Normal QA
                - Uno

         *
         *
         */

    }

    public static String getProperty(String key) {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        try {

            Properties properties = new Properties();;
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("test.properties");
            properties.load(inputStream);

            return properties.getProperty(key);

        } catch (Exception ex) {
            return "";
        }
    }

    public static Integer getPropertyInt(String key) {

        try {
            return Integer.parseInt(getProperty(key));
        } catch (Exception ex) {}

        return 1;
    }

}
