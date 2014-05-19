//
//  CoreDataHelper.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-19.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <CoreData/CoreData.h>

@interface CoreDataHelper : NSObject

@property NSManagedObjectContext * managedObjectContext;
@property NSManagedObjectModel * managedObjectModel;
@property NSPersistentStoreCoordinator * persistentStoreCoordinator;

@end
