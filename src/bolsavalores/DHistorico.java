package bolsavalores;

public class DHistorico 
{
    //Declaracion de Variables
    public String NombreValor;
    public String Fecha;
    public int    FechaYYYY;
    public int    FechaMM;
    public int    FechaDD;
    public double ValorCierre;
    public double Maximo;
    public double Minimo;
    public double Variacion;
    public double PorVariacion;
    public double PivotPoint;
    public double R1;
    public double S1;
    public double R2;
    public double S2;    
    
    //Constructor
    public DHistorico()
    {
        NombreValor  = "";
        Fecha        = "";
        FechaYYYY    = 0;
        FechaMM      = 0;
        FechaDD      = 0;
        ValorCierre  = 0;
        Maximo       = 0;
        Minimo       = 0;
        Variacion    = 0;
        PorVariacion = 0;
        PivotPoint   = 0;
        R1           = 0;
        S1           = 0;
        R2           = 0;
        S2           = 0;
    }
    
    //Guarda/Calcula Cada campo de la Cadena en sus Variables Correspondientes
    public void ObtieneValores(String PE_NombreValor, String PE_Cadena, int PE_Redondeo)
    {
        //Asignamos el Nombre del Valor
        NombreValor = PE_NombreValor;
        
        //Guarda en Variables Independientes todos los Campos de la Linea
        DesmenuzaLinea(PE_Cadena, PE_Redondeo);
        
        //Calcula el Resto de los Campos
        CalculaRestoValores(PE_Redondeo);
        
        //Separa el Campo Fecha
        SeparaFechaYYYYMMDD();
    }            
    
    //Calcula el Resto de Campos
    public void CalculaRestoValores(int PE_Redondeo)
    {        
        PivotPoint = Redondear((ValorCierre + Maximo + Minimo) / 3, PE_Redondeo);
        R1         = Redondear((PivotPoint * 2) - Minimo, PE_Redondeo);
        S1         = Redondear((PivotPoint * 2) - Maximo, PE_Redondeo);
        R2         = Redondear((PivotPoint - S1) + R1, PE_Redondeo);
        S2         = Redondear((PivotPoint - R1) + S1, PE_Redondeo);
    }
    
    //Obtiene los YYYY MM y DD de la Fecha en Variables por Separado
    public void SeparaFechaYYYYMMDD()
    {
        FechaYYYY = Integer.parseInt(Fecha.substring(0, 4));
        FechaMM   = Integer.parseInt(Fecha.substring(5, 7));
        FechaDD   = Integer.parseInt(Fecha.substring(8, 10));
    }

    //Guarda Cada campo de la Cadena en sus Variables Correspondientes
    public void DesmenuzaLinea(String PE_Cadena, int PE_Redondeo)
    {
        int Posicion;
        int Indice = 0;
        String AuxCampo;

        //Recuperamos la Fecha
        Posicion = PE_Cadena.indexOf('	', Indice);
        Fecha = PE_Cadena.substring(Indice, Posicion);
        
        //Recuperamos el Valor de Cierre
        Indice = Posicion + 1;
        Posicion = PE_Cadena.indexOf('	', Indice);
        AuxCampo = PE_Cadena.substring(Indice, Posicion);
        ValorCierre = Redondear(Double.parseDouble(AuxCampo.replace(",", ".")), PE_Redondeo);
        
        //Recuperamos la Variacion
        Indice = Posicion + 1;
        Posicion = PE_Cadena.indexOf('	', Indice);
        AuxCampo = PE_Cadena.substring(Indice, Posicion);
        Variacion = Redondear(Double.parseDouble(AuxCampo.replace(",", ".")), PE_Redondeo);
        
        //Recuperamos el % de Variacion
        Indice = Posicion + 1;
        Posicion = PE_Cadena.indexOf('	', Indice);
        AuxCampo = PE_Cadena.substring(Indice, Posicion);
        PorVariacion = Redondear(Double.parseDouble(AuxCampo.replace(",", ".")), PE_Redondeo);
        
        //Recuperamos el Maximo
        Indice = Posicion + 1;
        Posicion = PE_Cadena.indexOf('	', Indice);
        AuxCampo = PE_Cadena.substring(Indice, Posicion);
        Maximo = Redondear(Double.parseDouble(AuxCampo.replace(",", ".")), PE_Redondeo);
        if (Maximo == 0.0)
            Maximo = ValorCierre;
        
        //Recuperamos el Minimo
        Indice = Posicion + 1;
        Posicion = PE_Cadena.indexOf('	', Indice);
        AuxCampo = PE_Cadena.substring(Indice, Posicion);
        Minimo = Redondear(Double.parseDouble(AuxCampo.replace(",", ".")), PE_Redondeo);
        if (Minimo == 0.0)
            Minimo = ValorCierre;
    }
    
    private double Redondear(Double PE_Numero, int Decimales)
    {
        double Factor = Math.pow(10, Decimales);
        return (Math.round(PE_Numero * Factor) / Factor);
    }
}
