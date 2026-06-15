package com.marina.sistema_turismo.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Serviço para salvar arquivos enviados pelo usuário (fotos e PDFs) no disco do servidor
@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    // Executado automaticamente após o Spring inicializar o bean
    // Cria as pastas de upload na inicialização e registra o caminho absoluto no log
    @PostConstruct
    public void init() {
        try {
            Path fotosDir = Paths.get(uploadDir, "fotos").toAbsolutePath();
            Path roteirosDir = Paths.get(uploadDir, "roteiros").toAbsolutePath();
            Files.createDirectories(fotosDir);
            Files.createDirectories(roteirosDir);
            log.info("Diretório de uploads: {}", Paths.get(uploadDir).toAbsolutePath());
        } catch (IOException e) {
            log.error("Não foi possível criar os diretórios de upload: {}", e.getMessage());
        }
    }

    public String salvarFoto(MultipartFile file) throws IOException {
        return salvar(file, "fotos");
    }

    public String salvarRoteiro(MultipartFile file) throws IOException {
        return salvar(file, "roteiros");
    }

    // Salva uma lista de fotos e retorna os caminhos relativos de cada uma
    public List<String> salvarFotos(List<MultipartFile> files) throws IOException {
        List<String> caminhos = new ArrayList<>();
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                caminhos.add(salvarFoto(f));
            }
        }
        return caminhos;
    }

    private String salvar(MultipartFile file, String subdir) throws IOException {
        Path dir = Paths.get(uploadDir, subdir).toAbsolutePath();
        Files.createDirectories(dir);

        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }

        String nomeArquivo = UUID.randomUUID() + ext;
        Path destino = dir.resolve(nomeArquivo);
        file.transferTo(destino.toFile());

        log.info("Arquivo salvo: {}", destino);
        return subdir + "/" + nomeArquivo;
    }
}
