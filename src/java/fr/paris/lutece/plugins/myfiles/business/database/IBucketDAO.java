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
import fr.paris.lutece.util.ReferenceList;
import java.util.List;

/**
 * IBucketDAO Interface
 */
public interface IBucketDAO
{
    /**
     * Insert a new record in the table.
     * @param bucket instance of the Bucket object to insert
     * @param plugin the Plugin
     */
    void insert( Bucket bucket, Plugin plugin );

    /**
     * Update the record in the table
     * @param bucket the reference of the Bucket
     * @param plugin the Plugin
     */
    void store( Bucket bucket, Plugin plugin );

    /**
     * Delete a record from the table
     * @param strKey The identifier of the Bucket to delete
     * @param plugin the Plugin
     */
    void delete( String strKey, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param strKey The identifier of the bucket
     * @param plugin the Plugin
     * @return The instance of the bucket
     */
    Bucket load( String strKey, Plugin plugin );

    /**
     * Load the data of all the bucket objects and returns them as a list
     * @param plugin the Plugin
     * @return The list which contains the data of all the bucket objects
     */
    List<Bucket> selectBucketsList( Plugin plugin );
    
    /**
     * Load the id of all the bucket objects and returns them as a list
     * @param plugin the Plugin
     * @return The list which contains the id of all the bucket objects
     */
    List<String> selectIdBucketsList( Plugin plugin );

    /**
    * Returns true if the bucket exists
    * @param strKey The key of bucket
    * @param plugin The Plugin object
    * @return boolean the existance of the bucket
    */
    boolean bucketExists( String strKey, Plugin plugin );
}