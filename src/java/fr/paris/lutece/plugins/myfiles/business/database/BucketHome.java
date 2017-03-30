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
 * This class provides instances management methods (create, find, ...) for Bucket objects
 */
public final class BucketHome
{
    // Static variable pointed at the DAO instance
    private static IBucketDAO _dao = SpringContextService.getBean( "myfiles.bucketDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "myfiles" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private BucketHome(  )
    {
    }

    /**
     * Create an instance of the bucket class
     * @param bucket The instance of the Bucket which contains the informations to store
     * @return The  instance of bucket which has been created with its primary key.
     */
    public static Bucket create( Bucket bucket )
    {
        _dao.insert( bucket, _plugin );

        return bucket;
    }

    /**
     * Update of the bucket which is specified in parameter
     * @param bucket The instance of the Bucket which contains the data to store
     * @return The instance of the  bucket which has been updated
     */
    public static Bucket update( Bucket bucket )
    {
        _dao.store( bucket, _plugin );

        return bucket;
    }

    /**
     * Remove the bucket whose identifier is specified in parameter
     * @param nKey The bucket Id
     */
    public static void remove( String strKey )
    {
        _dao.delete( strKey, _plugin );
    }

    /**
     * Returns an instance of a bucket whose identifier is specified in parameter
     * @param nKey The bucket primary key
     * @return an instance of Bucket
     */
    public static Bucket findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey, _plugin);
    }

    /**
     * Load the data of all the bucket objects and returns them as a list
     * @return the list which contains the data of all the bucket objects
     */
    public static List<Bucket> getBucketsList( )
    {
        return _dao.selectBucketsList( _plugin );
    }
    
    /**
     * Load the id of all the bucket objects and returns them as a list
     * @return the list which contains the id of all the bucket objects
     */
    public static List<String> getIdBucketsList( )
    {
        return _dao.selectIdBucketsList( _plugin );
    }

    /**
    * Returns true if the bucket exists
    * @param strKey The key of bucket
    * @return boolean the existance of the bucket
    */
    public static boolean bucketExists( String strKey )
    {
        return _dao.bucketExists( strKey, _plugin );
    }
}

