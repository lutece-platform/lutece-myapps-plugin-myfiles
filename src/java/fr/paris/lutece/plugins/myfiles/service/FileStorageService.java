package fr.paris.lutece.plugins.myfiles.service;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
/**
 * FileStorageService
 */
public class FileStorageService
{

    private static final String PROPERTY_SERVER_URL = "myfiles.server.url";
    private static final String PROPERTY_ACCESS_KEY = "myfiles.server.access_key";
    private static final String PROPERTY_SECRET_KEY = "myfiles.server.secret_key";

    private static FileStorageService _singleton;
    private static MinioClient _client;

    /**
     * Private constructor
     */
    private FileStorageService()
    {
    }

    public static FileStorageService instance()
    {
        if( _singleton == null )
        {
            try
            {
                _singleton = new FileStorageService ( ); 
                String strServerUrl = AppPropertiesService.getProperty( PROPERTY_SERVER_URL );
                String strAccessKey = AppPropertiesService.getProperty( PROPERTY_ACCESS_KEY );
                String strSecretKey = AppPropertiesService.getProperty( PROPERTY_SECRET_KEY );
                _client = new MinioClient( strServerUrl, strAccessKey, strSecretKey );
            }
            catch( InvalidEndpointException | InvalidPortException ex )
            {
                AppLogService.error( "FileStorageService : error creating minio client : " + ex.getMessage(), ex );
            }

        }
        return _singleton;
    }

    public List<String> getFiles( String strUserId ) throws NoStorageException, InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, NoResponseException, XmlPullParserException, ErrorResponseException, InternalException
    {
        List<String> listFiles = new ArrayList<>();
        String strBucketName = strUserId;
        boolean bExist = _client.bucketExists( strBucketName );
        if( bExist )
        {
            for( Result<Item> result : _client.listObjects( strBucketName ))
            {
                listFiles.add( result.get().objectName() );
            }
        }
        else
        {
            throw new NoStorageException();
        }
        return listFiles;
    }

    public void createStorage( String strName )throws InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, NoResponseException, XmlPullParserException, ErrorResponseException, InternalException
    {
        _client.makeBucket( strName );
    }
    
}


