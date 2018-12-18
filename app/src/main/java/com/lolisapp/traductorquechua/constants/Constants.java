package com.lolisapp.traductorquechua.constants;

/**
 * Created by Carlos on 31/07/2015.
 * Clase donde se almacenan las constantes usadas en el programa
 */
public class Constants {

    //Tipo telefono
    public static final Integer TIPO_TELEFONO_CASA = 1;
    public static final Integer TIPO_CELULAR = 2;
    public static final Integer TIPO_OTROS = 3;
    public static final String ETIQUETA_TIPO_TELEFONO = "Teléfono de casa";
    public static final String ETIQUETA_TIPO_CELULAR = "Celular";
    public static final String ETIQUETA_TIPO_OTROS = "Otros";


    public static final String BUNDLE_EXPAND_IMAGE = "expand_image";

    public static final Integer ID_COMPROBANTE_TIPO_BOLETA = 1;
    public static final Integer ID_COMPROBANTE_TIPO_FACTURA = 2;
    public static final String ETIQUETA_TIPO_BOLETA = "Boleta";
    public static final String ETIQUETA_TIPO_FACTURA = "Factura";



    public static final String BUNDLE_ID_CATEGORIA_PRODUCT_CATALOG = "id_categoria";
    public static final String BUNDLE_PRODUCT_PRODUCT_CATALOG = "product";
    public static final String BUNDLE_ADD_DIRECCION_DIALOG_LIST_DEPARMENT = "listaDepartamentos";

    public static final String BUNDLE_PHONE_DIALOG = "phone";


    public static final String BUNDLE_CANASTA_ORDEN = "Orden";

    public static final String LUGAR_TIPO_DEPARTAMENTO = "DEPARTAMENTO";
    public static final String LUGAR_TIPO_PROVINCIA = "PROVINCIA";
    public static final String LUGAR_TIPO_DISTRITO = "DISTRITO";

    public static final Integer LIMITE_SUPERIOR_DEPARTAMENTO = 250000;
    public static final Integer LIMITE_INFERIOR_DEPARTAMENTO = 0;
    public static final Integer MOD_DEPARTAMENTO = 10000;


    public static final Integer TAM_LIMITE_DEPARTAMENTO = 9999;

    public static final Integer TAM_LIMITE_PROVINCIA = 9900;
    public static final Integer MOD_PROVINCIA = 100;

    public static final Integer TAM_LIMITE_DISTRITO = 99;
    public static final Integer MOD_DISTRITO = 1;

    public static final String USER_PRIVATE_PREFERENCES = "userPrivPref";
    public static final String USER_SESSION_KEY = "UserSessionKey";
    public static final String USER_CAMBIO_KEY = "UserCambioKey";

    public static final String URL_SEPARATOR = "/";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy h:mm a";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String SISTEMA_EXTERNO_FACEBOOK = "facebook";
    public static final String SISTEMA_EXTERNO_GOOGLE = "google";
    public static final String VACIO = "";


    public static final String TAG_MESSAGE_CODE = "code";
    public static final String TAG_MESSAGE_MESSAGE = "message";



    public static final Integer CATALOG_PAGINATION_QUANTITY = 20;
    public static final Integer CANTIDAD_COMPRAR_DEFECTO = 1;

    public static final String IMAGEN_TIPO_SLIDER = "3";

    public static final String IMAGEN_VISTA_DETALLE_TIPO_DETALLE = "4,5";
    public static final Integer ID_TIPO_DETALLE_ADICIONAL = 4;
    public static final Integer ID_TIPO_PRINCIPAL = 5;

    public static final Integer ID_IMAGEN_TIPO_CATALOGO = 3;
    public static final String IMAGEN_TIPO_CATALOGO = "3";

    public static final Integer ID_IMAGEN_LISTA = 3;
    public static final String IMAGEN_TIPO_VISTA_CANASTA = "3";


    public static final Integer CODIGO_CON_SEGUIMIENTO = 1;


    public static final int TIPO_PRODUCTO_LISTADO_TOTAL = 1;
    public static final int TIPO_PRODUCTO_BUSQUEDA = 2;
    public static final int TIPO_PRODUCTO_CATEGORIA = 3;

    //public static final String URL_IMAGES = "http://restventasadmin.sondeomovil.com/restventas/img/";
    //public static final String BASE_URL_IMAGEN = "http://restventasadmin.sondeomovil.com/restventas/img/";

    //    public static final String BASE_URL_SLIDER_IMAGEN="http://192.168.0.102/rest_modulo_venta/img/slider/";
 //   public static final String BASE_URL_SLIDER_IMAGEN = "http://restventasadmin.sondeomovil.com/restventas/img/slider/";


    public static final String SIMBOLO_SOLES = "S/ ";
    public static final String SIMBOLO_DOLARES = "$ ";

    public static final String TIPOCAMBIO_PRIVATE_PREFERENCES = "tipoCambioPref";

    public static final String CATALOG_SOURCE_TAG = "catalog_source_tag";

    public static final int CATALOG_SEARCH_SOURCE_VALUE = 1;
    public static final int CATALOG_CATEGORY_SOURCE_VALUE = 2;


    public static final String CATALOG_SEARCH_TAG_QUERY = "search_query";
    public static final String CATALOG_CATEGORY_TAG_SEARCH = "search_category";


    public static final String TIPOCAMBIO_DEFAULT = "4.000";

    public static final Integer MONEDA_TIPO_SOLES=1;
    public static final Integer MONEDA_TIPO_DOLARES=2;


    public static final String TEXTO_UNIDADES_ABREVIADO = "uds";
    public static final String TEXTO_UNIDAD_ABREVIADO = "ud";

    //Codigos de metodos de pago activos
    public static final int ID_METODO_PAGO_PAYPAL = 1;
    public static final int ID_METODO_PAGO_PAGO_EN_DOMICILIO = 2;

    //Configuraction Paypal
    // 1: desarrollo / 2:produccion
    public static final int PAYPAL_ENVIRONMENT_SELECTED = 1;

    public static final int PAYPAL_ENVIRONMENT_DESARROLLO = 1;
    public static final int PAYPAL_ENVIRONMENT_PRODUCCION = 2;

    //paypal desarrollo


    //paypal produccion
    public static final String TIMEZONE_UTC = "UTC";
    public static final String SIMPLE_HOUR_24H_FORMAT = "HH:mm";
    public static final String SIMPLE_YEAR_FORMAT = "yyyy";

    public static final Integer ESTADO_ORDEN_PAGADO = 2;

    public static final String PAYPAL_DATETIME_FORMAR = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    //Ids Categorias Padre
    public static final String ID_CATEGORIA_PADRE_RON = "11";
    public static final String ID_CATEGORIA_PADRE_CERVEZA = "10";
    public static final String ID_CATEGORIA_PADRE_WISKI = "12";
    public static final String ID_CATEGORIA_PADRE_PISCO = "13";
    public static final String ID_CATEGORIA_PADRE_VINO = "25";
    public static final String ID_CATEGORIA_PADRE_PROMOCIONES = "32";


}
