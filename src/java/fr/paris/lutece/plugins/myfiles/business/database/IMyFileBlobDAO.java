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
 * IMyFileBlobDAO Interface
 */
public interface IMyFileBlobDAO
{
    /**
     * Insert a new record in the table.
     * @param myFileBlob instance of the MyFileBlob object to insert
     * @param plugin the Plugin
     */
    void insert( MyFileBlob myFileBlob, Plugin plugin );

    /**
     * Update the record in the table
     * @param myFileBlob the reference of the MyFileBlob
     * @param plugin the Plugin
     */
    void store( MyFileBlob myFileBlob, Plugin plugin );

    /**
     * Delete a record from the table
     * @param nKey The identifier of the MyFileBlob to delete
     * @param plugin the Plugin
     */
    void delete( int nKey, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param nKey The identifier of the myFileBlob
     * @param plugin the Plugin
     * @return The instance of the myFileBlob
     */
    MyFileBlob load( int nKey, Plugin plugin );

    /**
     * Load the data from the table
     * @param strNameBucket The myFileBlob bucket name
     * @param strNameFile The myFileBlob name
     * @param plugin the Plugin
     * @return The instance of the myFileBlob
     */
    MyFileBlob loadByName( String strNameBucket, String strNameFile, Plugin plugin );

    /**
     * Load the data of all the myFileBlob objects and returns them as a list
     * @param plugin the Plugin
     * @return The list which contains the data of all the myFileBlob objects
     */
    List<MyFileBlob> selectMyFileBlobsList( Plugin plugin );
    
    /**
     * Load the id of all the myFileBlob objects and returns them as a list
     * @param plugin the Plugin
     * @return The list which contains the id of all the myFileBlob objects
     */
    List<Integer> selectIdMyFileBlobsList( Plugin plugin );

    /**
    * Returns true if the file blob exists
    * @param strNameBucket The key of bucket's file blob
    * @param strNameFile The key of file blob
    * @param plugin The Plugin object
    * @return boolean the existance of the bucket
    */
    boolean myFileBlobExists( String strNameBucket, String strNameFile, Plugin plugin );

    /**
    * Returns true if the bucket exists
    * @param strKey The key of bucket
    * @param plugin The Plugin object
    * @return boolean the existance of the bucket
    */
    boolean bucketExists( String strKey, Plugin plugin );
}