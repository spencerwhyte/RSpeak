//
//  CentralTabBarController.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CentralTabBarController : UITabBarController

@property (nonatomic) NSManagedObjectContext * managedObjectContext;

-(id)init;


@end