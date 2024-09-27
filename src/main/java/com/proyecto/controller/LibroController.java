
package com.proyecto.controller;

import com.proyecto.entity.Libro;
import com.proyecto.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibroController {
    //inyectamos nuestro servicio
    @Autowired
    private LibroService libroService;
    
    
    @GetMapping("/")
    public String listarTodosLosLibros(Model model){
        //nos permite mandar una lista u objeto a una vista para trabajarlas
        model.addAttribute("libros",libroService.findAllLibros());
        return "/listaLibros";
    }
 
    
    @GetMapping("/nuevoLibro")
    public String registrarNuevoLibro(){
        return "/registrarLibro";
    }
    
    @PostMapping("/guardarLibro")
    public String guardarLibro(Libro libro){
        libroService.saveLibro(libro);//despues de acceder al endpoint,guardamos el libro y retorna a la vista /listaLibros 
        return "redirect:/";
    }
    
    @GetMapping("/editarLibro/{id}")
    public String editarLibro(@PathVariable Long id,Model model){//pasamos el id y necesitamos un objeto tipo model para mandar un libro a nuestra vista
    Libro libro = libroService.findLibroById(id).get();//agregamos el get para que nos permita el metodo ya que retorna un optional
    model.addAttribute("libro",libro);//enviamos el atributo a la vista
        return "/editarLibro";
    }
    
    @PostMapping("/actualizarLibro")
    public String actualizarLibro(@RequestParam("idLibro") Long id,Libro libro){//requerimos como parametro el id que se encuentra oculto,lo pasamos como Long id y creamos un nuevo libro 
        libroService.updateLibro(id, libro);//le pasamos el id y el libro
        return "redirect:/";//redireccionamos a la raiz
    }
    
    @GetMapping("/eliminarLibro/{id}")
    public String eliminarLibro(@PathVariable Long id){ //pasamos el id del libro
        libroService.deleteLibroById(id); //lo eliminamos
        return "redirect:/"; //redireccionamos
    }
}
