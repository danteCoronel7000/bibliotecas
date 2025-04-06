package com.taller.bibliotecas.exceptions;


import com.taller.bibliotecas.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
public class FileManagerExceptionHandler{

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeExcept(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseMessage("El archivo excede los limites permitidos para subir imagenes"));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleFileNotFoundException (FileNotFoundException exc){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("El archivo solicitado no se ha encontrado o no esta disponible"));
    }

    //esta exception se leanzara cuando  al hacer  un upload no llegue ningun archivo, o el archivo este dañado
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseMessage> handleIOException (IOException exc){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error al procesar el archivo"));
    }


}
