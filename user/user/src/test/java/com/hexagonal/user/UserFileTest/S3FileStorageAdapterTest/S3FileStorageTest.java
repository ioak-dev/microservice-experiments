package com.hexagonal.user.UserFileTest.S3FileStorageAdapterTest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.hexagonal.user.infrastructure.filestorage.S3FileStorageAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {S3FileStorageAdapter.class})
class S3FileStorageTest {

  @Mock
  private AmazonS3 s3Client;

  @InjectMocks
  private S3FileStorageAdapter s3FileStorageAdapter;

  private final String bucketName = "test-bucket";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    s3FileStorageAdapter = new S3FileStorageAdapter(s3Client);
    s3FileStorageAdapter.bucketName = bucketName;
  }

  @Test
  void testUploadFile() throws IOException {

    MultipartFile file = mock(MultipartFile.class);
    when(file.getOriginalFilename()).thenReturn("test.txt");
    when(file.getSize()).thenReturn(10L);
    when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));

    String result = s3FileStorageAdapter.uploadFile(file, "user123");
    assertEquals("user123_test.txt", result);
    verify(s3Client, times(1)).putObject(eq(bucketName), eq("user123_test.txt"), any(), any(ObjectMetadata.class));
  }

  @Test
  void testDownloadFile() {

    S3Object s3Object = mock(S3Object.class);
    S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(new ByteArrayInputStream("test data".getBytes()), null);
    when(s3Client.getObject(bucketName, "user123_test.txt")).thenReturn(s3Object);

    when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);

    byte[] result = s3FileStorageAdapter.downloadFile("user123_test.txt", "user123");

    assertNotNull(result);
    assertArrayEquals("test data".getBytes(), result);
  }

  @Test
  void testDeleteFIle() {
    s3FileStorageAdapter.deleteFile("user123_test.txt", "user123");
    verify(s3Client, times(1)).deleteObject(bucketName, "user123_test.txt");
  }
}
