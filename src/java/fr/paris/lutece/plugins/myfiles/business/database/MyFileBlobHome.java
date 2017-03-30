/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
 package fr.paris.lutece.plugins.myfiles.business.database;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for MyFileBlob objects
 */
public final class MyFileBlobHome
{
    // Static variable pointed at the DAO instance
    private static IMyFileBlobDAO _dao = SpringContextService.getBean( "myfiles.myFileBlobDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "myfiles" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private MyFileBlobHome(  )
    {
    }

    /**
     * Create an instance of the myFileBlob class
     * @param myFileBlob The instance of the MyFileBlob which contains the informations to store
     * @return The  instance of myFileBlob which has been created with its primary key.
     */
    public static MyFileBlob create( MyFileBlob myFileBlob )
    {
        _dao.insert( myFileBlob, _plugin );

        return myFileBlob;
    }

    /**
     * Update of the myFileBlob which is specified in parameter
     * @param myFileBlob The instance of the MyFileBlob which contains the data to store
     * @return The instance of the  myFileBlob which has been updated
     */
    public static MyFileBlob update( MyFileBlob myFileBlob )
    {
        _dao.store( myFileBlob, _plugin );

        return myFileBlob;
    }

    /**
     * Remove the myFileBlob whose identifier is specified in parameter
     * @param nKey The myFileBlob Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a myFileBlob whose identifier is specified in parameter
     * @param nKey The myFileBlob primary key
     * @return an instance of MyFileBlob
     */
    public static MyFileBlob findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Returns an instance of a myFileBlob whose identifier is specified in parameter
     * @param strNameBucket The myFileBlob bucket name
     * @param strNameFile The myFileBlob name
     * @return an instance of MyFileBlob
     */
    public static MyFileBlob findByName( String strNameBucket, String strNameFile )
    {
        return _dao.loadByName( strNameBucket, strNameFile, _plugin);
    }

    /**
     * Load the data of all the myFileBlob objects and returns them as a list
     * @return the list which contains the data of all the myFileBlob objects
     */
    public static List<MyFileBlob> getMyFileBlobsList( )
    {
        return _dao.selectMyFileBlobsList( _plugin );
    }
    
    /**
     * Load the id of all the myFileBlob objects and returns them as a list
     * @return the list which contains the id of all the myFileBlob objects
     */
    public static List<Integer> getIdMyFileBlobsList( )
    {
        return _dao.selectIdMyFileBlobsList( _plugin );
    }

    /**
    * Returns true if the file blob exists
    * @param strNameBucket The key of bucket's file blob
    * @param strNameFile The key of file blob
    * @return boolean the existance of the bucket
    */
    public static boolean myFileBlobExists( String strNameBucket, String strNameFile )
    {
        return _dao.myFileBlobExists( strNameBucket, strNameFile, _plugin );
    }
}

