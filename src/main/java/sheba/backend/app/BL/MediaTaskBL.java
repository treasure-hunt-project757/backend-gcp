package sheba.backend.app.BL;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sheba.backend.app.entities.MediaTask;
import sheba.backend.app.entities.Task;
import sheba.backend.app.exceptions.MediaUploadFailed;
import sheba.backend.app.repositories.MediaTaskRepository;
import sheba.backend.app.util.StoragePath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class MediaTaskBL {
    private final MediaTaskRepository mediaTaskRepository;
    private final GcsBL gcsBL;

    public MediaTaskBL(MediaTaskRepository mediaTaskRepository, GcsBL gcsBL) {
        this.mediaTaskRepository = mediaTaskRepository;
        this.gcsBL = gcsBL;
    }

    //for cloud
    public MediaTask createMedia(Task task, MultipartFile file) throws MediaUploadFailed {
        try {
            MediaTask mediaTask = new MediaTask();
            String filename = generateUniqueFileName(task.getTaskID(), Objects.requireNonNull(file.getOriginalFilename()));
            mediaTask.setTask(task);
            mediaTask.setFileName(filename);
            mediaTask.setMediaType(file.getContentType());

            String objectName = StoragePath.MEDIA_TASK_PATH + "/task" + task.getTaskID();
            String gcsPath = gcsBL.bucketUpload(file, objectName);
            mediaTask.setMediaPath(gcsPath);
            System.out.println("url is " + gcsBL.getPublicUrl(gcsPath));
            mediaTask.setMediaUrl(gcsBL.getPublicUrl(gcsPath));

            return mediaTaskRepository.save(mediaTask);
        } catch (IOException e) {
            throw new MediaUploadFailed("Failed to upload media for task: " + task.getTaskID(), e);
        }
    }


    public void deleteMedia(MediaTask mediaTask) throws IOException {
        if (mediaTask != null && mediaTask.getMediaPath() != null) {
            gcsBL.bucketDelete(mediaTask.getMediaPath());
            mediaTaskRepository.delete(mediaTask);
        }
    }

    public void deleteAllMediaForTask(Long taskId) {
        List<MediaTask> mediaTasks = mediaTaskRepository.findAllByTaskId(taskId);
        for (MediaTask mediaTask : mediaTasks) {
            gcsBL.bucketDelete(mediaTask.getMediaPath());
            mediaTaskRepository.delete(mediaTask);
        }
    }


    private String storeFile(long taskId, MultipartFile file, String filename) throws IOException {
        String baseDirectory = StoragePath.MEDIA_TASK_PATH;
        Path taskDirectory = Paths.get(baseDirectory + File.separator + "task" + taskId);
        if (!Files.exists(taskDirectory)) {
            Files.createDirectories(taskDirectory);
        }

        Path filePath = taskDirectory.resolve(Objects.requireNonNull(filename));
        file.transferTo(filePath);

        return filePath.toString();
    }

    private void deleteMediaFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    private String generateUniqueFileName(long taskId, String originalFileName) {
        List<String> existingFileNames = mediaTaskRepository.findFileNamesByTaskId(taskId);
        String baseName = originalFileName;
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = originalFileName.substring(0, dotIndex);
            extension = originalFileName.substring(dotIndex);
        }
        String fileName = originalFileName;
        int i = 1;
        while (existingFileNames.contains(fileName)) {
            fileName = baseName + i + extension;
            i++;
        }
        return fileName;
    }

    public MediaTask duplicateMediaTask(Long originalMediaTaskID, Task newTask) {
        MediaTask originalMediaTask = mediaTaskRepository.findById(originalMediaTaskID).orElseThrow(() -> new EntityNotFoundException("Media not found with id " + originalMediaTaskID));
        MediaTask duplicatedMediaTask = new MediaTask();

        duplicatedMediaTask.setTask(newTask);
        duplicatedMediaTask.setFileName(originalMediaTask.getFileName());
        duplicatedMediaTask.setMediaPath(originalMediaTask.getMediaPath());
        duplicatedMediaTask.setMediaType(originalMediaTask.getMediaType());
        duplicatedMediaTask.setMediaUrl(originalMediaTask.getMediaUrl());

        return mediaTaskRepository.save(duplicatedMediaTask);
    }

}
