//
//  Message.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-19.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Message : NSManagedObject

@property (nonatomic, retain) NSString * senderDeviceID;
@property (nonatomic, retain) NSString * content;
@property (nonatomic, retain) NSDate * dateOfCreation;

@end
