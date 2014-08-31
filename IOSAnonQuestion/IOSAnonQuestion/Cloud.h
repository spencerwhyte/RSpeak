//
//  Cloud.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Question.h"
#import "AskQuestionDelegate.h"
#import "AFNetworking.h"
#import "DeviceInformationRequestDelegate.h"

#import "Settings.h"

@interface Cloud : NSObject

@property NSString * baseURL;
@property NSString * protocolVersion;
@property BOOL isSynchronizing;


-(void)askQuestion:(Question *)question delegate:(NSObject<AskQuestionDelegate> *)delegate;
-(void)synchronize;

+(Cloud*)sharedInstance;


@end
