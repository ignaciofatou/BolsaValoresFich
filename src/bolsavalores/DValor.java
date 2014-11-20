package bolsavalores;

public class DValor 
{
    //Declaracion de Variables
    public String NombreValor;
    public String URLHistorico;
    public int Redondeo;
    
    public DValor()
    {
        NombreValor = "";
        URLHistorico = "";
        Redondeo = 0;
    }
    
    //Guarda Cada campo de la Cadena en sus Variables Correspondientes
    public void DesmenuzaLinea(String PE_Cadena, int PE_RedondeoDef)
    {
        int Posicion;
        int Indice = 0;
        String AuxCampo;

        try
        {
            //Recuperamos el Nombre del Valor
            Posicion = PE_Cadena.indexOf('	', Indice);
            NombreValor = PE_Cadena.substring(Indice, Posicion);
        
            //Recuperamos la URL del Valor
            Indice = Posicion + 1;
            Posicion = PE_Cadena.indexOf('	', Indice);
            
            //Si luego no hay mas Datos
            if (Posicion == -1)
            {
                URLHistorico = PE_Cadena.substring(Indice);               
                Redondeo = PE_RedondeoDef;
            }
            //Si luego aparece el Redondeo (Opcional)
            else
            {
                URLHistorico = PE_Cadena.substring(Indice, Posicion);
                
                //Recuperamos el Redondeo del Valor
                Indice = Posicion + 1;
                AuxCampo = PE_Cadena.substring(Indice);
                Redondeo = Integer.parseInt(AuxCampo);
            }
        }
        catch (Exception e){System.out.println("Error: " + e.getMessage());}
    } 
}
