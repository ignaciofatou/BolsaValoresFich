package bolsavalores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class DValores 
{
    //Declaracion de Variables
    public int Rango;    
    public DValor [] Matrix;
    public int NumeroValores;
    private String FicheroValores;
    
    public DValores(int PE_Rango)
    {
        Rango  = PE_Rango;        
        Matrix = new DValor[PE_Rango];
        NumeroValores = 0;
        FicheroValores = "";
    }
    
    public void CargaFichValores(String PE_RutaFichVal, String PE_NomFichVal, int PE_RedondeoDef)
    {
        //Declaracion de Variables
        FicheroValores = PE_RutaFichVal + PE_NomFichVal;
        FileReader r = null;
        BufferedReader buffer;
        String AuxLinea;
        NumeroValores = 0;

        try
        {
            //Inicializacion de Variables
            r = new FileReader(FicheroValores);
            buffer = new BufferedReader(r);

            while (((AuxLinea = buffer.readLine()) != null) && (NumeroValores < Rango))
            {
                //Reservamos memoria para los Campos Correspondientes
                Matrix[NumeroValores] = new DValor();

                //Guardamos cada campo en su Variable Correspondiente
                Matrix[NumeroValores].DesmenuzaLinea(AuxLinea, PE_RedondeoDef);

                //Incrementamos el Numero de Linea
                NumeroValores++;
            }
            System.out.println("Fin de Fichero de Valores - Se han Leido "+NumeroValores+" Registros");
            
            //Ordenamos los Datos por Nombre Alfabeticamente
            OrdenarPorNombre();
            
            //Actualiza el Fichero
            ActualizarFichValores();
        }
        catch (Exception e){System.out.println("1Error: " + e.getMessage());}
        finally
        {
            try
            {
                if (r != null)
                    r.close();
            }
            catch (Exception e2){System.out.println("Error2: " + e2.getMessage());}
        }
    }
    
    public void RellenaCombo(javax.swing.JComboBox PE_ListaValores)
    {
        try
        {
            //Limpiamos el Combo
            javax.swing.JComboBox ComboNuevo = new javax.swing.JComboBox();
            PE_ListaValores.setModel(ComboNuevo.getModel());

            //Recorremos los Valores de Memoria y los Añadimos al Combo
            for (int x = 0; x < Rango; x++)
            {
                if (Matrix[x] != null)
                    PE_ListaValores.addItem(Matrix[x].NombreValor);
            }
            
            if (Matrix[0] != null)
                PE_ListaValores.setSelectedIndex(0);
        }
        catch (Exception e){System.out.println("1Error: " + e.getMessage());}
    }

    public void IncluirNuevoValor(String PE_NombreValor, String PE_URLHistorico, int PE_Redondeo)
    {
        //Comprobamos que no se ha superado el Máximo de Registros Historicos
        if (NumeroValores < Rango)
        {
            //Reservamos memoria para los Campos Correspondientes
            Matrix[NumeroValores] = new DValor();

            //Asignamos los Valores
            Matrix[NumeroValores].NombreValor  = PE_NombreValor;
            Matrix[NumeroValores].URLHistorico = PE_URLHistorico;
            Matrix[NumeroValores].Redondeo     = PE_Redondeo;
            
            //Incrementamos el Numero de Linea
            NumeroValores++;
            
            //Reordenamos de Nuevo la Lista
            OrdenarPorNombre();
            
            //Guardamos de Nuevo los Valores en Disco
            ActualizarFichValores();
        }
        //Si se ha superado mostramos un Mensaje de Error
        else
            System.out.println("Se han superado el Maximo de Valores Posibles");
    }
    
    public void EliminarValor(String PE_NombreValor)
    {
        int Posicion;
        
        //Si el Valor Existe en Memoria
        if ((Posicion = BuscarValor(PE_NombreValor)) != -1)
        {
            //Recorremos desde la Posicion del Valor a Eliminar en Adelante
            for (int x = Posicion; x < NumeroValores; x++)
            {
                //Vamos machacando la Posición Actual con la Posterior
                Matrix[x] = Matrix[x + 1];
            }
            //La Ultima Posicion la Ponemos a Nula
            Matrix[NumeroValores] = null;

            //Incrementamos el Numero de Linea
            NumeroValores--;
            
            //Reordenamos de Nuevo la Lista
            OrdenarPorNombre();
            
            //Guardamos de Nuevo los Valores en Disco
            ActualizarFichValores();
        }
        else
            System.out.println("El Valor <"+PE_NombreValor+"> No Existe en Memoria");
    }

    //Guarda los Datos de los Valores de Memoria en Fichero
    private void ActualizarFichValores()
    {
        //Declaracion de Variables
        File AuxFichero;
        FileWriter AuxEscritorArchivo;

        try
        {
            AuxFichero             = new File(FicheroValores);
            AuxEscritorArchivo     = new FileWriter(AuxFichero);
            BufferedWriter AuxBfWr = new BufferedWriter(AuxEscritorArchivo);
            PrintWriter AuxSalida  = new PrintWriter(AuxBfWr);

            for (int x = 0; x < NumeroValores; x++)
                AuxSalida.write(Matrix[x].NombreValor+'	'+Matrix[x].URLHistorico+'	'+Matrix[x].Redondeo+"\n");

            AuxSalida.close();
            AuxBfWr.close();
        }
        catch (Exception e){System.out.println("Error Guardando los Valores en Disco - " + e.getMessage());}
    }

    public int BuscarValor(String PE_NombreValor)
    {
        //Buscamos el Valor entre todos los Registros
        for (int x = 0; x < NumeroValores; x++)
            if (Matrix[x].NombreValor.equals(PE_NombreValor))
                return x;
        
        //Si No lo Encuentra -> -1
        return -1;
    }

    //Ordena los Datos de la Matriz por Fecha
    private void OrdenarPorNombre()
    {
        Arrays.sort(Matrix, 0, NumeroValores, new ComparaNombre());
    }

    //Implementamos el Metodo de Comparacion Necesario
    class ComparaNombre implements Comparator<DValor>
    {
        //Hacemos un Order Ascendente
        public int compare(DValor Campo1, DValor Campo2)
        {
            //Si Campo 1 = Campo 2
            if (Campo1.NombreValor.equals(Campo2.NombreValor))
                return 0;
            //Si Campo 1 Mayor que Campo 2
            else if (Campo1.NombreValor.compareTo(Campo2.NombreValor) > 0)
                return 1;
            //Si Campo 1 Menor que Campo 2
            else
                return -1;
        }
    }
}
