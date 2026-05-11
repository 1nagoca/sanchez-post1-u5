package com.ejemplo.servlet;

import com.ejemplo.model.tarea;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TareasServlet", urlPatterns = {"/tareas"})
public class TareasServlet extends HttpServlet {

    // Lista en memoria para guardar las tareas durante el laboratorio
    private final List<tarea> tareas = new ArrayList<>();
    private int contadorId = 1;

    @Override
    public void init() throws ServletException {
        // Cargamos un par de tareas de ejemplo al iniciar el servidor
        tareas.add(new tarea(contadorId++, "Leer documentación de Servlets"));
        tareas.add(new tarea(contadorId++, "Implementar ciclo GET/POST"));
    }

    /** GET /tareas - Muestra la lista de tareas */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Pasamos la lista de tareas a la vista (JSP)
        req.setAttribute("tareas", tareas);
        req.getRequestDispatcher("/WEB-INF/views/tareas.jsp").forward(req, resp);
    }

    /** POST /tareas - Agrega o elimina tareas */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");

        if ("agregar".equals(accion)) {
            String titulo = req.getParameter("titulo");

            // Validación simple en el servidor
            if (titulo == null || titulo.isBlank()) {
                req.setAttribute("error", "El título no puede estar vacío");
                req.setAttribute("tareas", tareas);
                req.getRequestDispatcher("/WEB-INF/views/tareas.jsp").forward(req, resp);
                return;
            }
            tareas.add(new tarea(contadorId++, titulo.trim()));

        } else if ("eliminar".equals(accion)) {
            int id = Integer.parseInt(req.getParameter("id"));
            tareas.removeIf(t -> t.getId() == id);
        }

        // Patrón PRG (Post/Redirect/Get): Redirigimos al GET para evitar duplicados al recargar
        resp.sendRedirect(req.getContextPath() + "/tareas");
    }
}