package com.hexagonal.user.UserFileTest.LocalFileStorageAdapterTest;


import com.hexagonal.user.infrastructure.filestorage.LocalFileStorageAdapter;
import com.hexagonal.user.infrastructure.filestorage.S3FileStorageAdapter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {S3FileStorageAdapter.class})
public class LocalFileStorageTest {

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  @Mock
  private MultipartFile mockFile;

  private LocalFileStorageAdapter localFileStorageAdapter;

  @Before
  public void setUp() {
    localFileStorageAdapter = new LocalFileStorageAdapter();
  }

  @Test
  public void testUploadFile() throws IOException {
    String userId = "user123";
    String fileName = "test.txt";
    when(mockFile.getOriginalFilename()).thenReturn(fileName);
    when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
    localFileStorageAdapter.setStoragePath(tempFolder.getRoot().getAbsolutePath());
    localFileStorageAdapter.uploadFile(mockFile, userId);
  }

  @Test
  public void testDownloadFile() throws IOException {
    String userId = "user123";
    String fileName = "user123_test.txt";
    byte[] fileContent = "test data".getBytes();
    Path tempFile = tempFolder.newFile(fileName).toPath();
    Files.write(tempFile, fileContent);
    localFileStorageAdapter.setStoragePath(tempFolder.getRoot().getAbsolutePath());
    localFileStorageAdapter.downloadFile(fileName, userId);
  }

  @Test
  public void testDeleteFile() throws IOException {
    String userId = "user123";
    String fileName = "user123_test.txt";
    Path tempFile = tempFolder.newFile(fileName).toPath();
    localFileStorageAdapter.setStoragePath(tempFolder.getRoot().getAbsolutePath());
    localFileStorageAdapter.deleteFile(fileName, userId);
  }
}
