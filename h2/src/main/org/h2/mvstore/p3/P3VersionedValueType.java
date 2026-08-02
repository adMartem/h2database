/**********************************************************************************************
 Copyright (c) 2010-2021 John M. Bradley and John R. Bradley ("AUTHORS"), All Rights Reserved.

 NOTICE:  All information contained herein is, and remains, the property of AUTHORS,
 their successors, and assigns. The intellectual and technical concepts contained herein
 are proprietary to AUTHORS and may be covered by U.S. and Foreign Patents or patents
 in process or pending, and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material is strictly
 forbidden unless express prior written permission is obtained from AUTHORS.
 Access to and use of the source code contained herein is hereby forbidden to anyone
 except those to whom express, written rights to use this source code have been granted
 by AUTHORS.

 The above copyright notice above does not evidence any actual or intended publication
 or disclosure of this source code, which includes information that is confidential
 and/or proprietary, and is a trade secret, of AUTHORS. ANY REPRODUCTION, MODIFICATION,
 DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE
 CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF AUTHORS IS STRICTLY PROHIBITED, AND IN
 VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION
 OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL
 ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 **********************************************************************************************/
package org.h2.mvstore.p3;

import org.h2.mvstore.tx.VersionedValueType;
import org.h2.mvstore.type.DataType;

/**
 * Versioned value type for P3 maps, wrapping {@link P3ValueType} by default.
 */
public class P3VersionedValueType<T> extends VersionedValueType<T, Object> {

    @SuppressWarnings("unchecked")
    public P3VersionedValueType() {
        super((DataType<T>) (DataType<?>) P3ValueType.INSTANCE);
    }

    public P3VersionedValueType(DataType<T> dataType) {
        super(dataType);
    }
}
