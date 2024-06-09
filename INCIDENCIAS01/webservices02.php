<?php
    require("config.php");
    $datos = array();
    $accion = "";
    if(isset($_POST["accion"])){
        $accion = $_POST["accion"];
    }

        
    if($accion == "registrar_usuario"){
    
        $nombre_completo = $_POST["nombre_completo"];
        $nombre_usuario = $_POST["nombre_usuario"];
        $password = $_POST["password"];
        $idTipoUsuario = $_POST["idTipoUsuario"];
        $correo = $_POST["correo"];

        if(registrar_usuario($nombre_completo,$nombre_usuario,$password,$idTipoUsuario,$correo) == 1){
            $datos["estado"] = 1;
            $datos["resultado"] = "Datos Almacenados con éxito"; 
        }else{
            $datos["estado"] = 0;
            $datos["resultado"] = "Error, no se pudo almacenar los datos"; 
        }        
    }

    else if ($accion == "editar_usuario"){
        $id_usuario = $_POST["id_usuario"];
        $nombre_completo = $_POST["nombre_completo"];
        $nombre_usuario = $_POST["nombre_usuario"];
        $password = $_POST["password"];
        $idTipoUsuario = $_POST["idTipoUsuario"];
        $correo = $_POST["correo"];

        if (editar_usuario($id_usuario, $nombre_completo, $nombre_usuario ,$password, $idTipoUsuario, $correo) == 1){
            $datos["estado"] = 1;
            $datos["resultado"] = "Datos modificados con exito";
        }else{
            $datos["estado"] = 0;
            $datos["resultado"] = "Error, no se pudo modificar los datos"; 
        }
    }

    else if ($accion == "eliminar_usuario"){
        $id_usuario = $_POST["id_usuario"];
        if (eliminar_usuario($id_usuario) == 1){
            $datos["estado"] = 1;
            $datos["resultado"] = "Usuario eliminado con éxito"; 
        }else{
            $datos["estado"] = 0;
            $datos["resultado"] = "Error, no se pudo eliminar el usuario"; 
        }
        
    }
    else if ($accion == "validar_usuario"){

        $user =$_POST['user'];
        $pass = $_POST['pass'];
       
       
            if($length = count( $datos["resultado"] = validar_usuario($user, $pass)) == 0){
                $datos["estado"] = 0;

               

            }else{
                $datos["estado"] = 1;
                $datos["resultado"] = validar_usuario($user, $pass);

            }

        
        
    

    }

    else if($accion == "listar_usuario"){
        $filtro = "";
        $idTipoUsuario = "";  // Nuevo campo para filtrar por género
        if(isset($_POST["filtro"])){
            $filtro = $_POST["filtro"]; 
        }
        if(isset($_POST["idTipoUsuario"])){
            $idTipoUsuario = $_POST["idTipoUsuario"]; 
        }
        
        $datos["estado"] = 1;
        $datos["resultado"] = listar_usuario($filtro, $idTipoUsuario);  // Pasamos el género como parámetro
        
    }
    else if($accion == "listar_incidencia"){
        $filtro = "";
        $idTipoIncidente = "";  // Nuevo campo para filtrar por género
        if(isset($_POST["filtro"])){
            $filtro = $_POST["filtro"]; 
        }
        if(isset($_POST["idTipoIncidente"])){
            $idTipoIncidente = $_POST["idTipoIncidente"]; 
        }
        
        $datos["estado"] = 1;
        $datos["resultado"] = listar_incidencia($filtro, $idTipoIncidente);  // Pasamos el género como parámetro
        
    }
    else if($accion == "listar_incidencia_emeplado"){
        $filtro = "";
        $idTipoIncidente = "";// Nuevo campo para filtrar por género
        $idEmpleado ="";  
        if(isset($_POST["filtro"])){
            $filtro = $_POST["filtro"]; 
        }
        if(isset($_POST["idTipoIncidente"])){
            $idTipoIncidente = $_POST["idTipoIncidente"]; 
        }
         $idEmpleado = (int)$_POST["idUsuario"];
         

        $datos["estado"] = 1;
        $datos["resultado"] = listar_incidencia_emeplado($filtro, $idTipoIncidente, $idEmpleado);  // Pasamos el género como parámetro
        
    }




    else if($accion == "obtener_usuario_por_id"){
        $id_usuario = (int)$_POST["id_usuario"];
        $datos["estado"] = 1;
        $datos["resultado"] = obtener_usuario_por_id($id_usuario);
    }

    else if ($accion == "obtener_incidencia_por_id"){
        $id_incidencia = (int)$_POST["id_incidencia"];
        $datos["estado"] = 1;
        $datos["resultado"] = obtener_incidencia_por_id( $id_incidencia );
    }

    else if ($accion == "agregar_incidencia"){
        $descripcion = $_POST["descripcion"];
        $fecha = $_POST["fecha"];//date("d-m-Y h:i:s");
        $idUsuario = $_POST["idUsuario"];
        $idTipoIncidente = $_POST["idTipoIncidente"];
        $img = $_POST["img"];

        if (agregrar_incidencia($descripcion,$fecha, $idUsuario, $idTipoIncidente,$img) == 1){
            $datos["estado"] = 1;
            $datos["resultado"] = "Incidente registrado correctamente"; 
        }else{
            $datos["estado"] = 0;
            $datos["resultado"] = "Error, no se pudo registrar el incidente"; 
        }        
    }

    else if ($accion == "editar_incidencia"){
        $idIncidente = $_POST["idIncidente"];
        $descripcionEdit = $_POST["descripcionEdit"];
        $fechaEdit = $_POST["fechaEdit"];
        $statusEdit = $_POST["statusEdit"];
        $idUsuarioEdit = $_POST["idUsuarioEdit"];
        $idTipoIncidenteEdit = $_POST["idTipoIncidenteEdit"];
        $imgEdit= $_POST["imgEdit"];

        if (editar_incidencia($idIncidente, $descripcionEdit, $fechaEdit, $statusEdit,$idUsuarioEdit, $idTipoIncidenteEdit, $imgEdit) == 1){
            $datos["estado"] = 1;
            $datos["resultado"] = "Datos modificados con exito";
        }else{
            $datos["estado"] = 0;
            $datos["resultado"] = "Error, no se pudo modificar los datos"; 
        }
    }

    else if ($accion == "eliminar_incidencia"){
        $idIncidente = $_POST["idIncidente"];

        if (eliminar_incidencia($idIncidente) == 1){
            $datos["estado"] = 1;
            $datos["resultado"] = "Incidencia eliminada con éxito"; 
        }else{
            $datos["estado"] = 0;
            $datos["resultado"] = "Error, no se pudo eliminar el registro"; 
        }
    }

    else if ($accion == "actualizar_estado_incidencia"){
        $id_incidencia = $_POST["id_incidencia"];
        if (actualizar_estado_incidencia($id_incidencia) == 1){
            $datos["estado"] = 1;
            $datos["resultado"] = "El estado se actualizo con exito";
        }else{
            $datos["estado"] = 0;
            $datos["resultado"] = "Error, no se pudo modificar el estado"; 
        }
    }
   

   
    
   
    
    else if ($accion == "listar_tipo_usuario") {
        $datos["estado"] = 1;
        $datos["resultado"] = listar_tipo_usuario();
    }
    
   /*
        else if($accion == "modificar"){
         $id_contacto = $_POST["id_contacto"];
         $nombre = $_POST["nombre"];
         $telefono = $_POST["telefono"];
         $genero = $_POST["genero"];  // Nuevo campo para el género
 
         if(modificarContacto($id_contacto, $nombre, $telefono, $genero) == 1){
             $datos["estado"] = 1;
             $datos["resultado"] = "Datos Modificados con éxito"; 
         }else{
             $datos["estado"] = 0;
             $datos["resultado"] = "Error, no se pudo modificar los datos"; 
         }
    }
    
    else if($accion == "eliminar"){
      $id_contacto = $_POST["id_contacto"];        
      if(eliminarContacto($id_contacto) == 1){
          $datos["estado"] = 1;
          $datos["resultado"] = "Datos Eliminados con éxito"; 
      }else{
          $datos["estado"] = 0;
          $datos["resultado"] = "Error, no se pudo eliminar los datos"; 
      }
  }*/

    echo json_encode($datos);

    ?>