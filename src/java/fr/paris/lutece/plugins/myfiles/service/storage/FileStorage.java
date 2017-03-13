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

package fr.paris.lutece.plugins.myfiles.service.storage;

import fr.paris.lutece.plugins.myfiles.business.MyFileData;
import fr.paris.lutece.plugins.myfiles.business.MyFileLink;
import java.util.List;

/**
 * FileStorage
 */
public interface FileStorage
{

    /**
     * Add a file to a storage space of a given user
     * 
     * @param strUserId
     *            The user ID
     * @param myFileData
     *            File data
     * @throws StorageException
     */
    void addFile( String strUserId, MyFileData myFileData ) throws StorageException;

    /**
     * Create a storage for agiven user
     * 
     * @param strNameId
     *            The User ID
     * @throws StorageException
     */
    void createStorage( String strNameId ) throws StorageException;

    /**
     * Gets all files for a given user
     * 
     * @param strUserId
     *            The user ID
     * @return The lest of files
     * @throws NoStorageException
     * @throws StorageException
     */
    List<MyFileLink> getFiles( String strUserId ) throws NoStorageException, StorageException;

    /**
     * Remove a file from the storage space of a given user
     * 
     * @param strUserId
     *            The User ID
     * @param strFilename
     *            The Filename
     * @throws StorageException
     */
    void removeFile( String strUserId, String strFilename ) throws StorageException;

}
