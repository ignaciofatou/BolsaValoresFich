package bolsavalores;

import java.io.BufferedReader;
import java.io.FileReader;

public class Configuracion
{
    //Variables Globales del Fichero de Condiguración
    public int MaximoValores;
    public int MaximoHistoricos;
    public int Redondeo;
    public int NumDiasGrafico;
    public String RutaDatos;
    public String FicheroValores;
    public String []DiasGrafico;    

    public Configuracion()
    {
        MaximoValores    = 0;
        MaximoHistoricos = 0;
        Redondeo         = 0;
        NumDiasGrafico   = 0;
        RutaDatos        = "";
        FicheroValores   = "";        
    }

    public void CargarCFG(String PE_NombreArchivo)
    {
        //Declaracion de Variables
        FileReader r = null;
        BufferedReader buffer;
        String AuxLinea;

        try
        {
            //Inicializacion de Variables
            r = new FileReader(PE_NombreArchivo);
            buffer = new BufferedReader(r);

            while ((AuxLinea = buffer.readLine()) != null)
            {
                //Si estamos sobre la Constante de Maximo de Valores
                if (AuxLinea.substring(0, 8).equals("MAXIMVAL"))
                    MaximoValores = Integer.parseInt(AuxLinea.substring(9));
                //Si estamos sobre la Constante de Maximo de Registros Historicos
                else if (AuxLinea.substring(0, 8).equals("MAXHISTO"))
                    MaximoHistoricos = Integer.parseInt(AuxLinea.substring(9));
                //Si estamos sobre la Constante de Redondeo
                else if (AuxLinea.substring(0, 8).equals("REDONDEO"))
                    Redondeo = Integer.parseInt(AuxLinea.substring(9));
                //Si estamos sobre la Constante de RutaDatos
                else if (AuxLinea.substring(0, 8).equals("RUTDATOS"))
                    RutaDatos = AuxLinea.substring(9);
                //Si estamos sobre la Constante de FicheroValores
                else if (AuxLinea.substring(0, 8).equals("FICHVALO"))                
                    FicheroValores = AuxLinea.substring(9);
                //Si estamos sobre la Constante de GraficosDias
                else if (AuxLinea.substring(0, 8).equals("GRAFDIAS"))
                {
                    String AuxCadena = AuxLinea.substring(9);
                    NumDiasGrafico = RecuperaNumDiasGrafico(AuxCadena);
                    //Si hay Datos
                    if (NumDiasGrafico != 0)
                    {
                        DiasGrafico = new String[NumDiasGrafico];
                        RecuperaDiasGrafico(AuxCadena);
                    }
                }
            }
        }
        catch (Exception e){System.out.println("Error Cargando Fichero de Configuracion: " + e.getMessage());}
        finally
        {
            try
            {
                if (r != null)
                    r.close();
            }
            catch (Exception e2){System.out.println("Error Cerrando Fichero de Configuracion: " + e2.getMessage());}
        }
    }

    //Carga los Combo Box con los Rangos de Dias Indicados en el CFG
    public void CargaBox(javax.swing.JComboBox PE_ComboBox)
    {
        //Limpiamos el Combo
        javax.swing.JComboBox AuxComboBox = new javax.swing.JComboBox();
        PE_ComboBox.setModel(AuxComboBox.getModel());

        //Si hay algun Dia para Cargar
        if (NumDiasGrafico != 0)
        {
            //Recorremos los Valores de Memoria y los Añadimos al Combo
            for (int x = 0; x < NumDiasGrafico; x++)
                PE_ComboBox.addItem(DiasGrafico[x]);
            
            //Seleccionamos el Primer Registro por Defecto
            PE_ComboBox.setSelectedIndex(0);
        }
    }

    private int RecuperaNumDiasGrafico(String PE_Cadena)
    {
        int NumDias = 0;
        int Posicion;
        int Indice = 0;

        //Mientras Existan Campos por Leer
        while ((Posicion = PE_Cadena.indexOf(',', Indice)) != -1)
        {
            Indice = Posicion + 1;
            NumDias ++;
        }
        //Si queda Otro Registro por Leer
        if (!PE_Cadena.substring(Indice).equals(""))
            NumDias ++;

        //Retornamos el Numero de Dias
        return NumDias;
    }

    private void RecuperaDiasGrafico(String PE_Cadena)
    {
        int NumDias = 0;
        int Posicion;
        int Indice = 0;
        String AuxCampo;

        //Mientras Existan Campos por Leer
        while ((Posicion = PE_Cadena.indexOf(',', Indice)) != -1)
        {
            //Recuperamos el Campo
            AuxCampo = PE_Cadena.substring(Indice, Posicion);
            DiasGrafico[NumDias] = AuxCampo;
            Indice = Posicion + 1;
            NumDias ++;
        }
        //Si queda Otro Registro por Leer
        if (!PE_Cadena.substring(Indice).equals(""))
        {
            AuxCampo = PE_Cadena.substring(Indice);
            DiasGrafico[NumDias] = AuxCampo;
        }
    }
}